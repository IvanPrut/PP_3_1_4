package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.security.PersonDetails;

@Controller
@RequestMapping("/user")
public class UserController {


    @GetMapping()
    public String getUserPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
            model.addAttribute("user", personDetails.getUserObj());
            return "user/user";
        } else {
            return "/service/error";
        }
    }
}
