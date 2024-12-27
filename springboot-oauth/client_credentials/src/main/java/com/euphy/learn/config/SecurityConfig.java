package com.euphy.learn.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    @Order(1)
    SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
          OAuth2AuthorizationServerConfigurer.authorizationServer();

        http
          // 設置了安全匹配器，確保這個配置只應用於授權伺服器的端點 (/oauth2/*)
          .securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
          .with(authorizationServerConfigurer, (authorizationServer) ->
            authorizationServer.oidc(withDefaults())	// Enable OpenID Connect 1.0
          )
          .authorizeHttpRequests((authorize) -> authorize
              .anyRequest().authenticated()
          )
          // Redirect to the login page when not authenticated from the
          // authorization endpoint
          .exceptionHandling((exceptions) -> exceptions
            .defaultAuthenticationEntryPointFor(
              new LoginUrlAuthenticationEntryPoint("/login"),
              new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
            )
          );
        return http.build();
    }

  @Bean
  @Order(2)
  SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
    http
      .authorizeHttpRequests(authorizeRequests -> authorizeRequests
        .anyRequest().authenticated())
      // Form login handles the redirect to the login page from the
      // authorization server filter chain
      .formLogin(withDefaults());
    return http.build();
  }
}
