package com.euphy.learn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CloudOauthServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(CloudOauthServerApplication.class, args);
    System.out.println("http://localhost:5566");
  }
}
