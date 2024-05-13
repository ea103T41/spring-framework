package com.learn.euphy.controller;

import com.learn.euphy.model.User;
import com.learn.euphy.model.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.accounts();
    }

    @PostMapping
    public void setUsers() {
        List<User> users = List.of(
                new User("admin", "12345678", "ea103t41@gmail.com", "admin"),
                new User("member", "12345678", "ea103t41@gmail.com", "member"),
                new User("guest", "12345678", "ea103t41@gmail.com", "guest")
        );
        userService.saveAll(users);
    }

}
