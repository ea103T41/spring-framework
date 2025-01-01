package com.euphy.learn.config;

import com.euphy.learn.password.OAuth2PasswordGrantAuthenticationConverter;
import com.euphy.learn.password.OAuth2PasswordGrantAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.DelegatingOAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2AccessTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2RefreshTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.UUID;

import static com.euphy.learn.password.OAuth2PasswordGrantAuthenticationConverter.PASSWORD_GRANT_TYPE;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  // Spring Security 內建密碼編碼器，但並未自動作為 @Bean 暴露，如果需要在應用中使用，必須顯式定義一個 @Bean
  // 預設使用 {bcrypt} 作為編碼方式
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Bean
  public OAuth2AuthorizationService authorizationService(RegisteredClientRepository registeredClientRepository) {
    return new InMemoryOAuth2AuthorizationService();
  }

  @Bean
  public OAuth2TokenGenerator<?> tokenGenerator() {
    OAuth2AccessTokenGenerator accessTokenGenerator = new OAuth2AccessTokenGenerator();
    OAuth2RefreshTokenGenerator refreshTokenGenerator = new OAuth2RefreshTokenGenerator();
    return new DelegatingOAuth2TokenGenerator(accessTokenGenerator, refreshTokenGenerator);
  }

  @Bean
  public SecurityFilterChain authorizationServerSecurityFilterChain(
    HttpSecurity http,
    UserDetailsService userDetailsService,
    OAuth2AuthorizationService authorizationService,
    OAuth2TokenGenerator<?> tokenGenerator) throws Exception {

    OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
      OAuth2AuthorizationServerConfigurer.authorizationServer();

    http
      .securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
      .with(authorizationServerConfigurer, (authorizationServer) ->
        authorizationServer
          .oidc(withDefaults())	// Enable OpenID Connect 1.0
          .tokenEndpoint(tokenEndpoint ->
            tokenEndpoint
              .accessTokenRequestConverter(new OAuth2PasswordGrantAuthenticationConverter())
              .authenticationProvider(new OAuth2PasswordGrantAuthenticationProvider(userDetailsService, passwordEncoder(), authorizationService, tokenGenerator))
          )
      )
      .authorizeHttpRequests((authorize) ->
        authorize
          .anyRequest().authenticated()
      );
    return http.build();
  }

  @Bean
  UserDetailsService users() {
    UserDetails user = User.builder()
      .username("admin")
      .password("{noop}password")
      .roles("role")
      .build();
    return new InMemoryUserDetailsManager(user);
  }

  @Bean
  public RegisteredClientRepository registeredClientRepository() {
    RegisteredClient clientPassword = RegisteredClient.withId(UUID.randomUUID().toString())
      .clientId("client-password")
      .clientSecret("{noop}654321")
      .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
      .authorizationGrantType(PASSWORD_GRANT_TYPE)
      .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
      .scope("user")
      .tokenSettings(TokenSettings.builder()
        // pass OAuth2AccessTokenGenerator.generate() method
        .accessTokenFormat(OAuth2TokenFormat.REFERENCE)
        .build())
      .build();

    return new InMemoryRegisteredClientRepository(clientPassword);
  }

}
