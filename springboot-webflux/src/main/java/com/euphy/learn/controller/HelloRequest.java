package com.euphy.learn.controller;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class HelloRequest {
  String name;
}
