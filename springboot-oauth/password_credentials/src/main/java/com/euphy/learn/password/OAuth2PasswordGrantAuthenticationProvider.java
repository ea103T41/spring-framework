package com.euphy.learn.password;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.euphy.learn.password.OAuth2PasswordGrantAuthenticationConverter.PASSWORD_GRANT_TYPE;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2PasswordGrantAuthenticationProvider implements AuthenticationProvider {

  @NonNull
  private final UserDetailsService userDetailsService;
  @NonNull
  private final PasswordEncoder passwordEncoder;
  @NonNull
  private final OAuth2AuthorizationService authorizationService;
  @NonNull
  private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    OAuth2PasswordGrantAuthenticationToken passwordGrantAuthenticationToken = (OAuth2PasswordGrantAuthenticationToken) authentication;

    // Ensure the client is authenticated
    OAuth2ClientAuthenticationToken clientPrincipal =
      getAuthenticatedClientElseThrowInvalidClient(passwordGrantAuthenticationToken);
    RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();


    // If the client is not registered or does not support grant type passwords
    if (registeredClient == null || !registeredClient.getAuthorizationGrantTypes().contains(passwordGrantAuthenticationToken.getGrantType())) {
      throw new OAuth2AuthenticationException(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT);
    }

    Set<String> authorizedScopes = Collections.emptySet();
    if (passwordGrantAuthenticationToken.getScopes() != null) {
      for (String requestedScope : passwordGrantAuthenticationToken.getScopes()) {
        if (!registeredClient.getScopes().contains(requestedScope)) {
          throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_SCOPE);
        }
      }
      authorizedScopes = new LinkedHashSet<>(passwordGrantAuthenticationToken.getScopes());
    }

    log.debug("Checking user credentials");
    // Check user exists and credentials are valid
    String providedUsername = passwordGrantAuthenticationToken.getUsername();
    String providedPassword = passwordGrantAuthenticationToken.getPassword();
    UserDetails userDetails = this.userDetailsService.loadUserByUsername(providedUsername);
    if (!this.passwordEncoder.matches(providedPassword, userDetails.getPassword())) {
      throw new OAuth2AuthenticationException("Invalid resource owner credentials");
    }

    var userPrincipal = new UsernamePasswordAuthenticationToken(userDetails, providedPassword, userDetails.getAuthorities());

    log.debug("Generating access token");
    //Generate the access token
    OAuth2TokenContext tokenContext = DefaultOAuth2TokenContext.builder()
      .registeredClient(registeredClient)
      .principal(userPrincipal)
      .authorizationServerContext(AuthorizationServerContextHolder.getContext())
      .authorizedScopes(authorizedScopes)
      .tokenType(OAuth2TokenType.ACCESS_TOKEN)
      .authorizationGrantType(PASSWORD_GRANT_TYPE)
      .authorizationGrant(passwordGrantAuthenticationToken)
      .put("roles", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()))
      .build();

    OAuth2Token generatedAccessToken = this.tokenGenerator.generate(tokenContext);
    if (generatedAccessToken == null) {
      OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
        "The token generator failed to generate the access token.", "ACCESS_TOKEN_REQUEST_ERROR_URI");
      throw new OAuth2AuthenticationException(error);
    }

    OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
      generatedAccessToken.getTokenValue(), generatedAccessToken.getIssuedAt(),
      generatedAccessToken.getExpiresAt(), tokenContext.getAuthorizedScopes());

    if (log.isDebugEnabled()) {
      log.debug("Creating authorization");
    }

    Map<String, Object> tokenMetadata = new HashMap<>();
    tokenMetadata.put("username", userDetails.getUsername());
    tokenMetadata.put("roles", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()));
    if (!authorizedScopes.isEmpty()) {
      tokenMetadata.put("scopes", authorizedScopes);
    }

    OAuth2Authorization authorization = OAuth2Authorization.withRegisteredClient(registeredClient)
      .principalName(userDetails.getUsername())
      .authorizationGrantType(PASSWORD_GRANT_TYPE)
      .token(accessToken, (metadata) -> metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME, tokenMetadata))
      .build();

    if (log.isDebugEnabled()) {
      log.debug("Saving authorization");
    }

    this.authorizationService.save(authorization);

    return new OAuth2AccessTokenAuthenticationToken(registeredClient, clientPrincipal, accessToken);
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return OAuth2PasswordGrantAuthenticationToken.class.isAssignableFrom(authentication);
  }

  private static OAuth2ClientAuthenticationToken getAuthenticatedClientElseThrowInvalidClient(Authentication authentication) {
    OAuth2ClientAuthenticationToken clientPrincipal = null;
    if (OAuth2ClientAuthenticationToken.class.isAssignableFrom(authentication.getPrincipal().getClass())) {
      clientPrincipal = (OAuth2ClientAuthenticationToken) authentication.getPrincipal();
    }
    if (clientPrincipal != null && clientPrincipal.isAuthenticated()) {
      return clientPrincipal;
    }
    throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_CLIENT);
  }
}
