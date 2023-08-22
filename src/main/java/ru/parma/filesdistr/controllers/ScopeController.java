package ru.parma.filesdistr.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.parma.filesdistr.dto.ScopeDto;
import ru.parma.filesdistr.dto.ScopePreviewDto;
import ru.parma.filesdistr.service.ScopeService;

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
    public ScopeDto get(@PathVariable Long scope_id) {
        return scopeService.getScopeById(scope_id);
    }

    @PutMapping("/update")
    @ResponseBody
    public void update(@RequestBody ScopeDto scope)
    {
        scopeService.update(scope);
    }

    @PostMapping("/add")
    @ResponseBody
    public void add (@RequestBody ScopeDto scope)
    {
        scopeService.add(scope);
    }

    @DeleteMapping("/delete/{scope_id}")
    @ResponseBody
    public void delete( @PathVariable Long scope_id)
    {
        scopeService.delete(scope_id);
    }
}





