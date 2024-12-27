package com.euphy.learn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OAuthClientCredentialsApplication {

    public static void main(String[] args) {
        SpringApplication.run(OAuthClientCredentialsApplication.class, args);
        System.out.println("http://localhost:9000/");
    }

}