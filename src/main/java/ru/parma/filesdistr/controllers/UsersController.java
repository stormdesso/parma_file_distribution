package ru.parma.filesdistr.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.parma.filesdistr.dto.AdminDto;
import ru.parma.filesdistr.dto.AdminScopeDto;
import ru.parma.filesdistr.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("users")
public class UsersController {
    private UserService userService;
    @Autowired
    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin/all")
    @ResponseBody
    public List<AdminDto> getAllAdmins() {
        return new ArrayList<>();
    }

    @GetMapping("/admin_scope/all")
    @ResponseBody
    public AdminScopeDto getAllAdminsScopes() {
        return new AdminScopeDto();
    }

    @GetMapping("/admin/{admin_id}")
    @ResponseBody
    public AdminDto get(@PathVariable String admin_id) {
        return new AdminDto();
    }

    @GetMapping("/admin_scope/{admin_scope_id}")
    @ResponseBody
    public AdminScopeDto getAdminScope( @PathVariable Long admin_scope_id) {
        return new AdminScopeDto();
    }

    @PutMapping("/admin/update")
    public void update(@RequestBody AdminDto adminDto)
    {
        //System.out.println("Изменения сохранены");
    }

    @PostMapping("/admin/add")
    public void add (@RequestBody AdminDto adminDto)
    {
        //System.out.println("Пространство добавлено");

    }

    @DeleteMapping("/admin/delete/{admin_id}")
    public void deleteAdmin( @PathVariable Long admin_id)
    {
        //System.out.println("Пространство удалено");
    }

    @PutMapping("/admin_scope/update")
    public void update(@RequestBody AdminScopeDto adminScopeDto)
    {
        //System.out.println("Изменения сохранены");
    }

    @PostMapping("/admin_scope/add")
    public void add (@RequestBody AdminScopeDto adminScopeDto)
    {
        //System.out.println("Пространство добавлено");

    }

    @DeleteMapping("/admin_scope/delete/{admin_scope_id}")
    public void deleteAdminScope( @PathVariable Long admin_scope_id)
    {
        //System.out.println("Пространство удалено");
    }
}





