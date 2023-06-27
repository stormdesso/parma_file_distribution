package ru.parma.filesdistr.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.parma.filesdistr.models.User;
import ru.parma.filesdistr.repos.UserRepo;


@Controller
@RequiredArgsConstructor
public class TestController {

    private final UserRepo userRepo;


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
