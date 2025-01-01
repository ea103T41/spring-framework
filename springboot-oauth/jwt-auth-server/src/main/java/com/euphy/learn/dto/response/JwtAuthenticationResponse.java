package com.euphy.learn.dto.response;

import com.euphy.learn.entity.Role;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class JwtAuthenticationResponse {
  String token;
  Integer expiresIn;
  Role role;
}
