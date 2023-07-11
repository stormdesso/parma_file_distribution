package ru.parma.filesdistr.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Role;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.parma.filesdistr.dto.ScopeDto;
import ru.parma.filesdistr.models.Roles;
import ru.parma.filesdistr.service.ScopeService;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("scope")
public class ScopeController {
    @Autowired
    private ScopeService scopeService;

    @GetMapping("/all")
    @ResponseBody
    public List<ScopeDto> getAll() {
        return scopeService.getAll();
    }
    @GetMapping("/{scope_id}")
    @ResponseBody
    public ScopeDto get(@PathVariable Long scope_id) {
        return scopeService.getScopeById(scope_id);
    }

    @PutMapping("/update")
    public void update(@RequestBody ScopeDto scope)
    {
        //System.out.println("Изменения сохранены");
    }

    @PostMapping("/add")
    public void add (@RequestBody ScopeDto scope)
    {
        //System.out.println("Пространство добавлено");

    }

    @DeleteMapping("/delete/{scope_id}")
    public void delete( @PathVariable Long scope_id)
    {
        //System.out.println("Пространство удалено");
    }
}





