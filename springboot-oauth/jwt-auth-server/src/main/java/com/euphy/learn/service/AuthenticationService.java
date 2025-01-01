package com.euphy.learn.service;

import com.euphy.learn.dto.request.SignUpRequest;
import com.euphy.learn.dto.request.SigninRequest;
import com.euphy.learn.dto.response.JwtAuthenticationResponse;

public interface AuthenticationService {
  JwtAuthenticationResponse signup(SignUpRequest request);
  JwtAuthenticationResponse signin(SigninRequest request);
}
