package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ServiceController {

    @GetMapping()
    public String getLoginPage() {
        return "service/login";
    }

    @GetMapping("/error")
    public String getErrorPage() {
        return "service/error";
    }
}
