package ru.parma.filesdistr.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.parma.filesdistr.dto.ScopeDto;
import ru.parma.filesdistr.dto.ScopePreviewDto;
import ru.parma.filesdistr.dto.UserCredentialsDto;
import ru.parma.filesdistr.service.ScopeService;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("scope")
public class ScopeController {

    private final ScopeService scopeService;
    @GetMapping("/all")
    @ResponseBody
    public List<ScopeDto> getAll() {
        return scopeService.getAll();
    }

    @GetMapping("/availableScopes")
    @ResponseBody
    public List<ScopePreviewDto> getAvailableScopes() {
        return scopeService.getAvailableScopes();
    }
    @GetMapping("/{scope_id}")
    @ResponseBody
    public ScopeDto get(@PathVariable Long scope_id) throws AccessDeniedException{
        return scopeService.getDto (scope_id);
    }

    @PutMapping("/update")
    @ResponseBody
    public void update(@RequestBody ScopeDto scope, UserCredentialsDto userCredentialsDto) throws AccessDeniedException{
        scopeService.update(scope, userCredentialsDto);
    }

    @PostMapping("/add")
    @ResponseBody
    public void add (@RequestBody ScopeDto scope, UserCredentialsDto userCredentialsDto) throws AccessDeniedException{
        scopeService.add(scope, userCredentialsDto);
    }

    @DeleteMapping("/delete/{scope_id}")
    @ResponseBody
    public void delete( @PathVariable Long scope_id) throws AccessDeniedException{
        scopeService.delete(scope_id);
    }
}





