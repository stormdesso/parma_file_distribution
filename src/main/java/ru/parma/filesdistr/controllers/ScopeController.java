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

//    @PutMapping("/admin/{userId}/scope/save")
//    @ResponseBody
//    public void update(@RequestBody ScopeDto scope)// @RequestBody Scope scope
//    {
//        System.out.println("Изменения сохранены");
//    }
}

        /*


    @PostMapping("/admin/{userId}/scope/add")
    @ResponseBody
    public void addAdminScope( @RequestBody Object scope)// @RequestBody Scope scope
    // admin
    public void add (@RequestBody ScopeDto scope)// @RequestBody Scope scope
    {
        System.out.println("Пространство добавлено");
        /*


    @DeleteMapping("/admin/{userId}/scope/delete")
    @ResponseBody
    public void deleteAdminScope( @RequestParam(name = "scopeId") Long id)
    // admin
    public void delete( @RequestParam(name = "scopeId") Long id)
    {
        System.out.println("Пространство удалено");
        */
