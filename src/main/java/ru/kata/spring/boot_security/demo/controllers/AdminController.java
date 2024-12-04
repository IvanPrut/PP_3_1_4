package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;
import ru.kata.spring.boot_security.demo.service.EditUserService;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EditUserService editUserService;
    private static final String ERROR = "/error";

    @Autowired
    public AdminController(UserRepository userRepository, RoleRepository roleRepository,
                           EditUserService editUserService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.editUserService = editUserService;
    }

    @GetMapping()
    public String admin(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "admin/users";
    }

    @GetMapping("/user/{id}")
    public String getUser(@PathVariable("id") Long id, Model model) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "admin/user";
        } else {
            return ERROR;
        }
    }

    @DeleteMapping("/user/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.delete(user.get());
            return "redirect:/admin";
        }
        return ERROR;
    }

    @GetMapping("/user/{id}/edit")
    public String giveEditUser(@PathVariable("id") Long id, Model model) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            model.addAttribute("allRoles", roleRepository.findAll());
            return "admin/edit";
        }
        return ERROR;
    }

    @PatchMapping("/user/{id}/edit")
    public String editUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("allRoles", roleRepository.findAll());
            return "admin/edit";
        }
        editUserService.editUser(user);
        return "redirect:/admin";
    }
}
