package ru.parma.filesdistr.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.parma.filesdistr.dto.AdminDto;
import ru.parma.filesdistr.dto.AdminScopeDto;
import ru.parma.filesdistr.dto.FolderDto;
import ru.parma.filesdistr.service.FolderService;
import ru.parma.filesdistr.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("users")
public class UsersController {
    @Autowired
    private UserService userService;

    @GetMapping("/admin/all")
    @ResponseBody
    public List<AdminDto> getAllAdmins() {
        return new ArrayList<AdminDto>();
    }

    @GetMapping("/admin_scope/all")
    @ResponseBody
    public AdminScopeDto getAllAdminsScopes() {
        return new AdminScopeDto();
    }

    @GetMapping("/admin/{admin_id}")
    @ResponseBody
    public AdminDto get() {
        return new AdminDto();
    }

    @GetMapping("/admin_scope/{admin_scope_id}")
    @ResponseBody
    public AdminScopeDto getAdminScope( @PathVariable Long admin_scope_id) {
        return new AdminScopeDto();
    }

    @PutMapping("/update/admin")
    public void update(@RequestBody AdminDto adminDto)
    {
        //System.out.println("Изменения сохранены");
    }

    @PostMapping("/add/admin")
    public void add (@RequestBody AdminDto adminDto)
    {
        //System.out.println("Пространство добавлено");

    }

    @DeleteMapping("/delete/admin/{admin_id}")
    public void deleteAdmin( @PathVariable Long admin_id)
    {
        //System.out.println("Пространство удалено");
    }

    @PutMapping("/update/admin_scope")
    public void update(@RequestBody AdminScopeDto adminScopeDto)
    {
        //System.out.println("Изменения сохранены");
    }

    @PostMapping("/add/admin_scope")
    public void add (@RequestBody AdminScopeDto adminScopeDto)
    {
        //System.out.println("Пространство добавлено");

    }

    @DeleteMapping("/delete/admin_scope/{admin_scope_id}")
    public void deleteAdminScope( @PathVariable Long admin_scope_id)
    {
        //System.out.println("Пространство удалено");
    }
}





