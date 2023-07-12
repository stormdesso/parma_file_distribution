package ru.parma.filesdistr.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.parma.filesdistr.dto.FolderDto;
import ru.parma.filesdistr.dto.VersionDto;
import ru.parma.filesdistr.service.FolderService;
import ru.parma.filesdistr.service.VersionService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("version")
public class VersionController {
    private VersionService versionService;
    @Autowired
    public VersionController(VersionService versionService) {
        this.versionService = versionService;
    }

    @GetMapping("/all/{scope_id}/{folder_id}")
    @ResponseBody
    public List<VersionDto> getAll(@PathVariable Long scope_id, @PathVariable Long folder_id) {
        return new ArrayList<VersionDto>();
    }

    @GetMapping("/{scope_id}/{folder_id}/{version_id}")
    @ResponseBody
    public VersionDto get(@PathVariable Long scope_id,
                          @PathVariable Long folder_id,
                          @PathVariable Long version_id) {
        return new VersionDto();
    }

    @PutMapping("/update")
    public void update(@RequestBody VersionDto versionDto)
    {
        //System.out.println("Изменения сохранены");
    }

    @PostMapping("/add")
    public void add (@RequestBody VersionDto versionDto)
    {
        //System.out.println("Пространство добавлено");

    }

    @DeleteMapping("/delete/{scope_id}/{folder_id}/{version_id}")
    public void delete(@PathVariable Long scope_id,
                       @PathVariable Long folder_id,
                       @PathVariable Long version_id)
    {
        //System.out.println("Пространство удалено");
    }
}





