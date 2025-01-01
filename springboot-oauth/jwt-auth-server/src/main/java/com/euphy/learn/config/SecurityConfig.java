package com.euphy.learn.config;

import com.euphy.learn.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  @NonNull
  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  @NonNull
  private final UserService userService;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
      // stateless REST API would not be affected by csrf attack
      .csrf(AbstractHttpConfigurer::disable)
      // Allow H2 Console's framework to load
      .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
      .authorizeHttpRequests(request -> request
          .requestMatchers("/api/v1/auth/**").permitAll()
          .requestMatchers("/h2-console/**").permitAll()
          .anyRequest().authenticated())
      // 由 Spring Security 自動檢測Bean並注入
      // .authenticationProvider(authenticationProvider())
      // extract username and password and then update them to SecurityContextHolder in JwtAuthenticationFilter
      .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
      .build();
  }

  @Bean
  // Spring Security 預設的 PasswordEncoder 不會自動暴露為 Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Bean
  // 使用自定義的用戶驗證邏輯，由 Spring Security 自動檢測並注入它們
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userService.userDetailsService()); // 改成客製的service
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }

  /*
   * Spring Security 預設的 AuthenticationManager 不會自動暴露為 Bean
   * 所以需要顯式定義，供其他類別通過 @Autowired 注入使用
   */
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
    throws Exception {
    return config.getAuthenticationManager();
  }

}
