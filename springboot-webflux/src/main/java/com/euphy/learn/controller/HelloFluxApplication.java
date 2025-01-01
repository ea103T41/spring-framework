package com.euphy.learn.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/hello")
public class HelloFluxApplication {

    @GetMapping("/{name}")
    public Mono<String> hello(@PathVariable("name") String name) {
        return Mono.just("Hello " + name + "!");
    }

    @PostMapping
    public Mono<String> hello(@RequestBody HelloRequest helloRequest) {
        return Mono.just("Hello " + helloRequest.getName() + "!");
    }
}
