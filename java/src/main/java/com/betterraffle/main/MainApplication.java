package com.betterraffle.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MainApplication {

    public static void main(String[] args) {
        startWebServer(args);

    }

    private static void startWebServer(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

}
