package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;
import ru.kata.spring.boot_security.demo.security.PersonDetails;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserValidator userValidator;
    private final UserService userService;
    private static final String ERROR = "/error";
    private static final String ADMIN_REDIRECT = "redirect:/admin";

    @Autowired
    public AdminController(UserRepository userRepository, RoleRepository roleRepository,
                            UserValidator userValidator, UserService userService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userValidator = userValidator;
        this.userService = userService;
    }

    @GetMapping()
    public String admin(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
            model.addAttribute("user", personDetails.getUserObj());
        } else return ERROR;
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("allRoles", roleRepository.findAll());
        return "admin/admin";
    }

    @PatchMapping("/users/{id}")
    public String userEdit(@ModelAttribute User user) {
        userService.prepareAndSafe(user);
        return ADMIN_REDIRECT;
    }

    @DeleteMapping("/users/{id}")
    public String userDelete (@PathVariable long id) {
        userRepository.deleteById(id);
        return ADMIN_REDIRECT;
    }

    @PostMapping("/users")
    public String userAdd(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return "service/error";
        }
        userService.prepareAndSafe(user);
        return ADMIN_REDIRECT;
    }
}
