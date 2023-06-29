package ru.parma.filesdistr.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.parma.filesdistr.dto.ScopeDto;
import ru.parma.filesdistr.service.ScopeService;

import java.util.List;

@Controller
@RequestMapping("scopes")
public class ScopeController {

    @Autowired
    private ScopeService scopeService;

    /*
    scopeController
    /scope/{id}

    folderController
    /folder/all/{scopeId} => [{id...}, {id...}]
    /folder/{id}

    fileController
    /file/all/{folderId}
    /file/all/{scopeId}
    /file/{id}


     */
    @GetMapping("/")
    @ResponseBody
    //@PreAuthorize()
    public List<ScopeDto> getAll() {
        // get current user id
        //if (guest) scopeService.getAll();
        long userId = 5;//context;
        List<ScopeDto> scopes = scopeService.get(userId);

        return scopes;
    }

    //Only read

    /**
     * Guest API
     **/
    // @GetMapping("/guest")
    @GetMapping("/{id}")
    @ResponseBody
    public String get() {

        /*
         * связь с бд
         */
        return "";
    }

    //CRUD

    /**
     * Admin API
     **/
    @GetMapping("/admin/{userId}/scope")
    @ResponseBody
    public String getAdminScopes() {

        /*
         * связь с бд
         * */

        //return new List<Scope>();
        return "admin scopes";
    }

    @PutMapping("/admin/{userId}/scope/save")
    @ResponseBody
    public void update(@RequestBody ScopeDto scope)// @RequestBody Scope scope
    {
        System.out.println("Изменения сохранены");
    }
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
