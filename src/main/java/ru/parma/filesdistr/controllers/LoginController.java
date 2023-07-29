package ru.parma.filesdistr.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/get_id")
    public Authentication getId (){
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @GetMapping("/logout")
    public String logout() {
        return "logout";
    }
}
