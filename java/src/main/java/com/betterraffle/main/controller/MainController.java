package com.betterraffle.main.controller;

import com.betterraffle.main.db.SQLHelper;
import com.betterraffle.main.entities.User;
import com.betterraffle.main.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class MainController {

    private final StorageService storageService;

    @CrossOrigin(origins = "http://10.48.69.70:8080")
    @RequestMapping("/signin")
    public User user(@RequestParam(value = "username") String userName) {
        System.out.println("received request from " + userName);
        return SQLHelper.fillToken(userName.trim());
    }

    @Autowired
    public MainController(StorageService storageService) {
        this.storageService = storageService;
    }

    @RequestMapping("/pickwinner")
    public User user() {
        User user = SQLHelper.pickWinner();
        if (user != null) {
            return user;
        }
        return new User();
    }
}
