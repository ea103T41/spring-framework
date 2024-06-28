package com.euphy.learn.controller;

import com.euphy.learn.model.Member;
import com.euphy.learn.model.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MemberController(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public List<Member> getMembers() {
        return memberRepository.findAll();
    }

    @PostMapping
    public String createMember(@RequestBody Member member) {
        String encodedPwd = passwordEncoder.encode(member.getPassword());
        member.setPassword(encodedPwd);
        member.setId(null);
        memberRepository.insert(member);
        return member.getId();
    }

}
