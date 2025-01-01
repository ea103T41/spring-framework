package com.euphy.learn.dto.request;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SignUpRequest {
  String firstName;
  String lastName;
  String email;
  String password;
  String role;
}
