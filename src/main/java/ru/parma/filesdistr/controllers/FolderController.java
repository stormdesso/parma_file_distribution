package ru.parma.filesdistr.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.parma.filesdistr.dto.FolderDto;
import ru.parma.filesdistr.service.FolderService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("folder")
public class FolderController {
    private FolderService folderService;

    @Autowired
    public FolderController(FolderService folderService) {
        this.folderService = folderService;
    }

    @GetMapping("/all/{scope_id}")
    @ResponseBody
    public List<FolderDto> getAll(@PathVariable Long scope_id) {

        return new ArrayList<>();
    }

    @GetMapping("/{folder_id}")
    @ResponseBody
    public FolderDto get(@PathVariable Long folder_id) {
        return new FolderDto();
    }

    @PutMapping("/update")
    public void update(@RequestBody FolderDto folderDto)
    {
        //System.out.println("Изменения сохранены");
    }

    @PostMapping("/add")
    public void add (@RequestBody FolderDto folderDto)
    {
        //System.out.println("Пространство добавлено");

    }

    @DeleteMapping("/delete/{folder_id}")
    public void delete(@PathVariable Long folder_id)
    {
        //System.out.println("Пространство удалено");
    }
}





