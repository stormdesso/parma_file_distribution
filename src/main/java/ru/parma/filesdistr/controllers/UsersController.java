package ru.parma.filesdistr.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.parma.filesdistr.dto.AdminDto;
import ru.parma.filesdistr.dto.AdminScopeDto;
import ru.parma.filesdistr.enums.Roles;
import ru.parma.filesdistr.models.User;
import ru.parma.filesdistr.repos.UserRepository;
import ru.parma.filesdistr.service.CustomUserDetailsService;
import ru.parma.filesdistr.service.UserService;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UsersController {
    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping("/admin/all")
    @ResponseBody
    public Set<User> getTree () {
        Set<User> users = new HashSet<>();
        Long id = CustomUserDetailsService.getAuthorizedUserId();
        User currUser = userRepository.getReferenceById(id);
        Set<Roles> rolesSet = currUser.getRoles();

        if(rolesSet.contains(Roles.ROOT)){
            users.addAll(userRepository.findByIdIsNot(id));
        }
        else
            if(rolesSet.contains(Roles.ADMIN)){
                if(currUser.isAdminManager()){
                    users.addAll(userRepository.findByIdIsNotAndAdminManagerIsTrue(id));
                }
                if(currUser.isAdminScopeManager()){
                    users.addAll(userRepository.findByIdIsNotAndAdminScopeManagerIsTrue(id));
                }
        }

        return users;
    }


    @PutMapping("/admin/update")
    public void update (@RequestBody AdminDto adminDto) {
        //System.out.println("Изменения сохранены");
    }

    @PostMapping("/admin/add")
    public void add (@RequestBody AdminDto adminDto) {
        //System.out.println("Пространство добавлено");

    }

    @DeleteMapping("/admin/delete/{admin_id}")
    public void deleteAdmin (@PathVariable Long admin_id) {
        //System.out.println("Пространство удалено");
    }

    @PutMapping("/admin_scope/update")
    public void update (@RequestBody AdminScopeDto adminScopeDto) {
        //System.out.println("Изменения сохранены");
    }

    @PostMapping("/admin_scope/add")
    public void add (@RequestBody AdminScopeDto adminScopeDto) {
        //System.out.println("Пространство добавлено");

    }

    @DeleteMapping("/admin_scope/delete/{admin_scope_id}")
    public void deleteAdminScope (@PathVariable Long admin_scope_id) {
        //System.out.println("Пространство удалено");
    }
}





