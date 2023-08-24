package ru.parma.filesdistr.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.parma.filesdistr.enums.Roles;
import ru.parma.filesdistr.models.User;
import ru.parma.filesdistr.service.CustomUserDetailsService;

import java.util.Set;


@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class LoginController {
    @GetMapping("/get_roles")
    public Set<Roles> getRoles() {
        if(CustomUserDetailsService.isAuthenticated()) {
            return ((User) (CustomUserDetailsService.getAuthentication().getPrincipal())).getRoles();
        }
        else
            return null;
    }
}
