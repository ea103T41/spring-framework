package com.euphy.learn.password;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class OAuth2PasswordGrantAuthenticationConverter implements AuthenticationConverter {

  public static final AuthorizationGrantType PASSWORD_GRANT_TYPE = AuthorizationGrantType.PASSWORD;
  @Override
  public Authentication convert(HttpServletRequest request) {
    String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
    if (!"password".equals(grantType)) {
      return null; // 只處理 password grant
    }

    String username = request.getParameter(OAuth2ParameterNames.USERNAME);
    String password = request.getParameter(OAuth2ParameterNames.PASSWORD);
    String scope = request.getParameter(OAuth2ParameterNames.SCOPE);
    if (username == null || password == null) {
      throw new IllegalArgumentException("Missing username or password");
    }

    Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();
    Set<String> scopes = scope != null ? Set.of(scope.split(" ")) : null;

    return new OAuth2PasswordGrantAuthenticationToken(clientPrincipal, username, password, scopes);
  }
}
