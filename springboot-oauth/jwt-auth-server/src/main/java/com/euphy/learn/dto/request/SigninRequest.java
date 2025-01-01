package com.euphy.learn.dto.request;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SigninRequest {
  String email;
  String password;
}
