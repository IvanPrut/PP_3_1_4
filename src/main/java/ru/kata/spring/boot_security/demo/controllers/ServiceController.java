package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ServiceController {

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @GetMapping()
    public String getIndexPage() {
        return "index";
    }
}
