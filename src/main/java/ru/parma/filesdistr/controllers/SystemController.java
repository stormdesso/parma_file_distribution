package ru.parma.filesdistr.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class SystemController {
    private final Environment environment;

    @GetMapping("/version")
    public String getVersion() {
        return environment.getProperty("api.version");
    }
}
