package ru.parma.filesdistr.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.parma.filesdistr.dto.VersionDto;
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

    @GetMapping("/all/{folder_id}")
    @ResponseBody
    public List<VersionDto> getAll(@PathVariable Long folder_id) {
        return new ArrayList<>();
    }

    @GetMapping("/{version_id}")
    @ResponseBody
    public VersionDto get(@PathVariable Long version_id) {
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

    @DeleteMapping("/delete/{version_id}")
    public void delete(@PathVariable Long version_id)
    {
        //System.out.println("Пространство удалено");
    }
}





