package com.euphy.learn.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailManager() {
        UserDetails user1 = User.withUsername("user")
                .password("{bcrypt}$2a$10$OK4UTs4XSi6Jm.jdYHPvleKwDPFt3a9LiAK3MrjyU/KtcLEbZl6Em")
                .authorities("USER")
                .build();
        UserDetails user2 = User.withUsername("admin")
                .password("{sha256}97cde38028ad898ebc02e690819fa220e88c62e0699403e94fff291cfffaf8410849f27605abcbc0")
                .authorities("ADMIN")
                .build();
        UserDetails user3 = User.withUsername("guest")
                .password("{noop}123456")
                .authorities("GUEST")
                .build();
        return new InMemoryUserDetailsManager(List.of(user1, user2, user3));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .formLogin(Customizer.withDefaults())
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(HttpMethod.GET, "/", "/guest/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/user/**").hasAuthority("USER")
                        .requestMatchers(HttpMethod.GET, "/admin/**").hasAuthority("ADMIN")
                        .requestMatchers("/**").authenticated()
                )
                .exceptionHandling(customizer -> customizer.accessDeniedPage("/error/403.html"))
                .build();
    }

}
