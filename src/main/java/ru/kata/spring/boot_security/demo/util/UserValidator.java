package ru.kata.spring.boot_security.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.UserCurrentDetailsService;

@Component
public class UserValidator implements Validator {

    private final UserCurrentDetailsService userCurrentDetailsService;

    @Autowired
    public UserValidator(UserCurrentDetailsService userCurrentDetailsService) {
        this.userCurrentDetailsService = userCurrentDetailsService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        try {
            userCurrentDetailsService.loadUserByUsername(user.getUsername());
        } catch (UsernameNotFoundException ignored) {
            return;
        }

        errors.rejectValue("username", "",
                "Человек с таким именем пользователя уже существует");
    }
}
