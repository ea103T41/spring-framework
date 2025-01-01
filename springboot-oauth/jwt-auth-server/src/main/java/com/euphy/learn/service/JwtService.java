package com.euphy.learn.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
  String extractUserName(String token);
  Integer extractExpiresIn(String token);
  String generateToken(UserDetails userDetails);
  boolean isTokenValid(String token);
}
