package ru.parma.filesdistr.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.parma.filesdistr.models.User;
import ru.parma.filesdistr.repos.UserRepo;


@RestController
@RequiredArgsConstructor
public class TestController {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/encrypt")
    public String encrypt(@RequestParam("psw") String psw) {
        return passwordEncoder.encode(psw);
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/hello")
    @ResponseBody
    public String hello() {

        return "permit all";
    }

    @GetMapping("/user")
    public String user() {

        return "user";
    }

    @GetMapping("/admin")
    @ResponseBody
    public String admin() {

        return "admin";
    }

    @GetMapping("/jpa")
    @ResponseBody
    public String jpa() {
        User user = userRepo.findByName("test");
        return "jpa";
    }

}
