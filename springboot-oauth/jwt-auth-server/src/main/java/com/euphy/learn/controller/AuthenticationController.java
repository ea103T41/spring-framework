package com.euphy.learn.controller;

import com.euphy.learn.dto.request.SignUpRequest;
import com.euphy.learn.dto.request.SigninRequest;
import com.euphy.learn.dto.response.JwtAuthenticationResponse;
import com.euphy.learn.service.AuthenticationService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  @NonNull
  private final AuthenticationService authenticationService;

  @PostMapping("/signup")
  public JwtAuthenticationResponse signup(@RequestBody SignUpRequest request) {
    return authenticationService.signup(request);
  }

  @PostMapping("/signin")
  public JwtAuthenticationResponse signin(@RequestBody SigninRequest request) {
    return authenticationService.signin(request);
  }
}
