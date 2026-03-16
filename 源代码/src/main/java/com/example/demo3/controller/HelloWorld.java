package com.example.demo3.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")  // 添加API前缀
public class HelloWorld {

    @GetMapping("/hello")
    public String helloWorld() {
        return "Hello SpringBoot!";
    }

    @GetMapping("/greet")
    public String greet(String name) {
        if (name == null || name.trim().isEmpty()) {
            name = "World";
        }
        return "Hello " + name + "!";
    }
}