package ru.kata.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final String USER = "USER";
    private static final String ADMIN = "ADMIN";

    @Autowired
    public DataInitializer(RoleRepository roleRepository, UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        Role userRole = roleRepository.findByRoleName(USER)
                .orElseGet(() -> roleRepository.saveAndFlush(new Role(USER)));
        Role adminRole = roleRepository.findByRoleName(ADMIN).
                orElseGet(() -> roleRepository.saveAndFlush(new Role(ADMIN)));

        if (userRepository.count() == 0) {
            Set<Role> roles = new HashSet<>();
            roles.add(userRole);
            roles.add(adminRole);

            User adminUser = new User(ADMIN, ADMIN, 100, "admin@admin.com",
                    passwordEncoder.encode(ADMIN));
            userRepository.saveAndFlush(adminUser);
            adminUser.setRoles(roles);
            userRepository.saveAndFlush(adminUser);
        }
    }
}
