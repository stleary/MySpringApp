package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@SpringBootApplication
@RestController
public class Example {

    private final GreetingService greetingService;

    @Autowired
    public Example(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    public static void main(String[] args) {
        SpringApplication.run(Example.class, args);
    }

    @GetMapping("/greeting")
    public String index(@RequestParam(defaultValue = "unknown") String name) {
        return greetingService.greet(name);
    }
    @GetMapping("/")
    public String index() {
        return "Greetings from Spring Boot! /";
    }
}