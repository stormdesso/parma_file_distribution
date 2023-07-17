package ru.parma.filesdistr.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.parma.filesdistr.service.FileLocationService;

//если не работает, запись файлов -> включить админские права для IDEA

@Controller
@RequiredArgsConstructor
@RequestMapping("myFiles")
public class FileController {
    private final FileLocationService fileLocationService;

    //TODO: async подгрузка и загрузка
    //TODO: связка с version

    @PostMapping("/upload")
    @ResponseBody
    void upload(@RequestParam MultipartFile file) throws Exception {
        fileLocationService.save(file.getBytes(), file.getOriginalFilename());
    }

    @GetMapping(value = "/download/{fileId}", produces = MediaType.ALL_VALUE)
    @ResponseBody
    FileSystemResource download(@PathVariable Long fileId){
        return fileLocationService.get(fileId);
    }
}





