package com.learn.euphy.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GuestController {

    @GetMapping("/guest")
    public String guest() {
        return "guest";
    }

}
