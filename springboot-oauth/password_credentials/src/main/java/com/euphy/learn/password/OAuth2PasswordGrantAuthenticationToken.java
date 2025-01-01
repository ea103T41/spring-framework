package com.euphy.learn.password;

import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;

import java.util.Set;

import static com.euphy.learn.password.OAuth2PasswordGrantAuthenticationConverter.PASSWORD_GRANT_TYPE;

@Getter
public class OAuth2PasswordGrantAuthenticationToken extends OAuth2AuthorizationGrantAuthenticationToken {

  private final String username;
  private final String password;
  private final String clientId;
  private final Set<String> scopes;

  protected OAuth2PasswordGrantAuthenticationToken(Authentication clientPrincipal,
                                                   String username,
                                                   String password,
                                                   Set<String> scopes) {
    super(PASSWORD_GRANT_TYPE, clientPrincipal, null);
    this.username = username;
    this.password = password;
    this.clientId = clientPrincipal.getName();
    this.scopes = scopes;
  }

}
