package ru.parma.filesdistr.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.parma.filesdistr.dto.FolderDto;
import ru.parma.filesdistr.service.FolderService;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("folder")
public class FolderController {
    private final FolderService folderService;

    @GetMapping("/all/{scope_id}")
    @ResponseBody
    public List<FolderDto> getAll(@PathVariable Long scope_id) {
        return folderService.getAll(scope_id);
    }

    @GetMapping("/{folder_id}")
    @ResponseBody
    public FolderDto get(@PathVariable Long folder_id) {
        return folderService.getDto (folder_id);
    }

    @PutMapping("/update")
    @ResponseBody
    public void update(@RequestBody FolderDto folderDto) throws AccessDeniedException{
        folderService.update(folderDto);
    }

    @PostMapping("/add/{scope_id}")
    @ResponseBody
    public void add (@RequestBody FolderDto folderDto, @PathVariable long scope_id) throws AccessDeniedException{
        folderService.add(folderDto, scope_id);
    }

    @DeleteMapping("/delete/{folder_id}")
    @ResponseBody
    public void delete(@PathVariable Long folder_id)
    {
        folderService.delete(folder_id);
    }
}





