package ru.kata.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kata.spring.boot_security.demo.service.UserCurrentDetailsService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final SuccessUserHandler successUserHandler;
    private final UserCurrentDetailsService userCurrentDetailsService;
    private static final String LOGIN = "/auth/login";

    @Autowired
    public WebSecurityConfig(SuccessUserHandler successUserHandler,
                             UserCurrentDetailsService userCurrentDetailsService) {
        this.successUserHandler = successUserHandler;
        this.userCurrentDetailsService = userCurrentDetailsService;
    }

    public WebSecurityConfig(boolean disableDefaults, SuccessUserHandler successUserHandler, UserCurrentDetailsService userCurrentDetailsService) {
        super(disableDefaults);
        this.successUserHandler = successUserHandler;
        this.userCurrentDetailsService = userCurrentDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/admin/**", "/admin").hasRole("ADMIN")
                .antMatchers(LOGIN, "/auth/registration").anonymous()
                .antMatchers("error").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage(LOGIN)
                .loginProcessingUrl("/process_login")
                .failureUrl("/auth/login?error=true")
                .successHandler(successUserHandler)
                .permitAll()
                .and()
                .logout().logoutUrl("/auth/logout").logoutSuccessUrl(LOGIN)
                .permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userCurrentDetailsService).passwordEncoder(getPasswordEncoder());
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}