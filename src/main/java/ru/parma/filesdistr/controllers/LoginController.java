package ru.parma.filesdistr.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


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
