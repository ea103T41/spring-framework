package com.euphy.learn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OAuth2AuthorizationServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(OAuth2AuthorizationServerApplication.class, args);
        System.out.println("http://localhost:9000/");
    }

}