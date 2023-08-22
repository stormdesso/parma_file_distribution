package ru.parma.filesdistr.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.parma.filesdistr.dto.TagDto;
import ru.parma.filesdistr.service.AppearanceService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("settings/appearance")
public class AppearanceController {

    private final AppearanceService appearanceService;

    @GetMapping("/all")
    @ResponseBody
    public List<TagDto> getAll() {
        return appearanceService.getAll();
    }


    @PostMapping("/add")
    @ResponseBody
    public void add (@RequestBody TagDto tagDto)
    {
        appearanceService.add (tagDto);
    }

    //TODO: вылетает 405
    @DeleteMapping("/delete/{tagId}")
    @ResponseBody
    public void delete(@PathVariable Long tagId)
    {
        appearanceService.delete (tagId);
    }
}





