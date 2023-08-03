package ru.parma.filesdistr.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.parma.filesdistr.dto.VersionDto;
import ru.parma.filesdistr.service.VersionService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("version")
public class VersionController {
    private final VersionService versionService;
    @GetMapping("/all/{folder_id}")
    @ResponseBody
    public List<VersionDto> getAll(@PathVariable Long folder_id) {
        return versionService.getAll(folder_id);
    }

    @GetMapping("/{version_id}")
    @ResponseBody
    public VersionDto get(@PathVariable Long version_id) {
        return versionService.get(version_id);
    }

    @PutMapping("/update")
    public void update(@RequestBody VersionDto versionDto)
    {
        versionService.update(versionDto);
    }

    @PostMapping("/add/{folder_id}")
    public void add (@RequestBody VersionDto versionDto, @PathVariable long folder_id)
    {
        versionService.add(versionDto, folder_id);

    }

    @DeleteMapping("/delete/{version_id}")
    public void delete(@PathVariable Long version_id)
    {
        versionService.delete(version_id);
    }
}





