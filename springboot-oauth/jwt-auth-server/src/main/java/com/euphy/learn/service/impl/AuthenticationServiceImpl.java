package com.euphy.learn.service.impl;

import com.euphy.learn.dto.request.SignUpRequest;
import com.euphy.learn.dto.request.SigninRequest;
import com.euphy.learn.dto.response.JwtAuthenticationResponse;
import com.euphy.learn.entity.Role;
import com.euphy.learn.entity.User;
import com.euphy.learn.repository.UserRepository;
import com.euphy.learn.service.AuthenticationService;
import com.euphy.learn.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  @Override
  public JwtAuthenticationResponse signup(SignUpRequest request) {
    var user = buildUser(request);
    userRepository.save(user);
    var jwt = jwtService.generateToken(user);
    return JwtAuthenticationResponse.builder()
      .token(jwt)
      .expiresIn(jwtService.extractExpiresIn(jwt))
      .role(user.getRole())
      .build();
  }

  @Override
  public JwtAuthenticationResponse signin(SigninRequest request) {
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
    var user = userRepository.findByEmail(request.getEmail())
                             .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
    var jwt = jwtService.generateToken(user);
    return JwtAuthenticationResponse.builder()
      .token(jwt)
      .expiresIn(jwtService.extractExpiresIn(jwt))
      .role(user.getRole())
      .build();
  }

  private User buildUser(SignUpRequest request) {
    String role = Optional.ofNullable(request.getRole())
      .map(String::trim)
      .filter(r -> !r.isEmpty())
      .orElse(Role.USER.name());
    String password = Optional.ofNullable(request.getPassword())
      .orElseThrow(() -> new IllegalArgumentException("Password is required."));

    return User.builder()
      .firstName(request.getFirstName())
      .lastName(request.getLastName())
      .email(request.getEmail())
      .password(passwordEncoder.encode(password))
      .role(Role.valueOf(role))
      .build();
  }
}
