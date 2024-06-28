package com.euphy.learn.controller;

import com.euphy.learn.model.Member;
import com.euphy.learn.model.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HomeController {

    @Autowired
    private MemberRepository memberRepository;

    @GetMapping("/")
    public String hello(
            @RequestParam(value = "name", defaultValue = "World") String name) {
        return "Hello, " + name + "!";
    }

    @GetMapping("/reset")
    public List<Member> reset() {
        memberRepository.deleteAll();
        return memberRepository.findAll();
    }
}
