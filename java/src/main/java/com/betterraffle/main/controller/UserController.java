package com.betterraffle.main.controller;

import com.betterraffle.main.entities.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserController {

    @RequestMapping("/signin")
    public User user(@RequestParam(value = "name", defaultValue = "World") String name) {
        User user = new User();
        user.setName(name);
        return user;
    }
}
