package com.baeldung;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OAuth2ClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(OAuth2ClientApplication.class, args);
        System.out.println("http://127.0.0.1:8080/articles");
    }

}