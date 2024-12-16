package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class APIAdminController {

    private final UserRepository userRepository;
    private final UserService userService;

    public APIAdminController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    // Получение всех пользователей
    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(users);
    }


    // Получение пользователя по ID
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(value -> ResponseEntity.ok().body(value)).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Сохранение нового пользователя
    @PostMapping("/users")
    public ResponseEntity<String> addUser(@Valid @RequestBody User user) {
        Optional<User> userOptional = userRepository.findByEmail(user.getEmail());
        if (userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with this email already exists.");
        } else {
            userService.prepareAndSafe(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("User added successfully.");
        }
    }

    // Удаление пользователя по ID
    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.deleteById(id);
            return ResponseEntity.ok().body("User deleted successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Редактирование пользователя по ID
    @PutMapping("/users/{id}")
    public ResponseEntity<String> updateUser(@PathVariable long id, @Valid @RequestBody User user) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            userService.prepareAndSafe(user);
            return ResponseEntity.ok().body("User updated successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
