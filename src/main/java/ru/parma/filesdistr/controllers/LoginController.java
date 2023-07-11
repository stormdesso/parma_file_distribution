package ru.parma.filesdistr.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.parma.filesdistr.models.User;
import ru.parma.filesdistr.repos.UserRepository;


@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/perform_login")
    public void performLogin(@RequestParam String username,
                             @RequestParam String password ) {

    }

    @GetMapping("/logout")
    public String logout() {
        return "logout";
    }
}
