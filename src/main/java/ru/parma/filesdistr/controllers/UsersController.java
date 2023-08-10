package ru.parma.filesdistr.controllers;


import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.parma.filesdistr.dto.AdminDto;
import ru.parma.filesdistr.dto.AdminScopeDto;
import ru.parma.filesdistr.service.UserService;

import java.util.Set;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UsersController {
    private final UserService userService;
    @GetMapping("/get_all_admins")
    @ResponseBody
    public Set<AdminDto> getAllAdmins () {
        return userService.getAllAdmins ();
    }

    @GetMapping("/get_all_admins_scopes")
    @ResponseBody
    public Set<AdminScopeDto> getAllAdminsScopes () {
        return userService.getAllAdminsScopes();
    }

    @PostMapping("/admin/add")
    @ResponseBody
    public void add (@RequestBody @NotNull AdminDto adminDto) {
        userService.add(adminDto);
    }

    @PutMapping("/admin/update")
    @ResponseBody
    public void updateAdmin (@RequestBody @NotNull AdminDto adminDto) {
        userService.updateAdmin(adminDto);
    }

    @DeleteMapping("/admin/delete/{adminId}")
    @ResponseBody
    public void deleteAdmin (@PathVariable Long adminId) {
        userService.deleteAdmin(adminId);
    }


    @PostMapping("/admin_scope/add")
    @ResponseBody
    public void add (@RequestBody @NotNull AdminScopeDto adminScopeDto) {
        userService.add(adminScopeDto);
    }

    @PutMapping("/admin_scopes/update")
    @ResponseBody
    public void updateAdminScopes (@RequestBody @NotNull AdminScopeDto adminScopeDto) {
        userService.updateAdminScopes(adminScopeDto);
    }

    @DeleteMapping("/admin_scope/delete/{adminScopeId}")
    @ResponseBody
    public void deleteAdminScope (@PathVariable Long adminScopeId) {
        userService.deleteAdminScope(adminScopeId);
    }
}





