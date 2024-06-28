package com.euphy.learn.config;

import com.euphy.learn.model.MemberAuthority;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    static final String ADMIN = MemberAuthority.ADMIN.name();
    static final String USER = MemberAuthority.USER.name();

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .formLogin(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(HttpMethod.GET, "/", "/guest/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/reset").permitAll()
                        .requestMatchers(HttpMethod.POST, "/member").permitAll()
                        .requestMatchers("/member/**").hasAuthority(ADMIN)
                        .requestMatchers(HttpMethod.GET, "/user/**").hasAuthority(USER)
                        .requestMatchers(HttpMethod.GET, "/admin/**").hasAuthority(ADMIN)
                        .requestMatchers("/**").authenticated()
                )
                .exceptionHandling(customizer -> customizer.accessDeniedPage("/public.error/403.html"))
                .build();
    }

}
