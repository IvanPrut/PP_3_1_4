package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.Optional;

@Service
public class EditUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public EditUserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void editUser(User user) {
        Optional<User> oldUser = userRepository.findById(user.getId());
        if (oldUser.isPresent()) {
            oldUser.get().setName(user.getName());
            oldUser.get().setPassword(passwordEncoder.encode(user.getPassword()));
            oldUser.get().setEmail(user.getEmail());
            oldUser.get().setRoles(user.getRoles());
            oldUser.get().setUsername(user.getUsername());
            oldUser.get().setSurname(user.getSurname());
            userRepository.save(oldUser.get());
        } else {
            System.out.println("User not found");
        }
    }
}
