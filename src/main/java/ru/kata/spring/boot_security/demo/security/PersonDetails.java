package ru.kata.spring.boot_security.demo.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.ArrayList;
import java.util.Collection;

public class PersonDetails implements UserDetails {

    private final User userObj;

    public PersonDetails(User userObj) {
        this.userObj = userObj;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>(this.userObj.getRoles());
    }

    @Override
    public String getPassword() {
        return userObj.getPassword();
    }

    @Override
    public String getUsername() {
        return userObj.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public User getUserObj() {
        return userObj;
    }
}
