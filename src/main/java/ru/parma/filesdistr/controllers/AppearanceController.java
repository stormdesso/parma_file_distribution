package ru.parma.filesdistr.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.parma.filesdistr.dto.TagDto;
import ru.parma.filesdistr.service.AppearanceService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("settings/appearance")
public class AppearanceController {

    private AppearanceService appearanceService;

    @Autowired
    public AppearanceController(AppearanceService appearanceService) {
        this.appearanceService = appearanceService;
    }

    @GetMapping("/all")
    @ResponseBody
    public List<TagDto> getAll() {
        return new ArrayList<TagDto>();
    }


    @PostMapping("/add")
    public void add (@RequestBody TagDto tagDto)
    {
        //System.out.println("Пространство добавлено");
    }

    @DeleteMapping("/delete/{tag_id}")
    public void delete(@PathVariable Long tag_id)
    {
        //System.out.println("Пространство удалено");
    }
}





