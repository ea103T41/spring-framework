package com.euphy.learn.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    @Order(1)
    SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
            .oidc(withDefaults());    // Enable OpenID Connect 1.0
        return http.formLogin(withDefaults()).build();
    }

    /*
     * authorizationServerSecurityFilterChain 是專門為 OAuth2 授權伺服器配置安全;
     * 處理與 authorizationServerConfigurer 定義的端點匹配的請求，例如：
        * /oauth2/authorize
        * /oauth2/token
        * /oauth2/jwks
     * defaultSecurityFilterChain 則是為所有其他請求配置了一個基本的安全過濾鏈，要求登錄才能訪問
     */

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

    @Bean
    UserDetailsService users() {
        // storing in-memory password using BCrypt
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        UserDetails user = User.builder()
            .username("admin")
            .password("password")
            .passwordEncoder(encoder::encode)
            .roles("USER")
            .build();
        return new InMemoryUserDetailsManager(user);
    }

}
