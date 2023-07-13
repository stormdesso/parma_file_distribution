package ru.parma.filesdistr.controllers;

import lombok.RequiredArgsConstructor;
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

    private final AppearanceService appearanceService;

    @GetMapping("/all")
    @ResponseBody
    public List<TagDto> getAll() {
        return new ArrayList<>();
    }


    @PostMapping("/add")
    public void add (@RequestBody TagDto tagDto)
    {
        //System.out.println("Пространство добавлено");
    }

    //TODO: вылетает 405
    @DeleteMapping("/delete/{tag_id}")
    public void delete(@PathVariable Integer tag_id)
    {
        //System.out.println("Пространство удалено");
    }
}





