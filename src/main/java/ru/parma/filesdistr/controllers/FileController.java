package ru.parma.filesdistr.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.parma.filesdistr.dto.FileDto;
import ru.parma.filesdistr.service.FileLocationService;
import org.apache.commons.io.FilenameUtils;

//если не работает запись файлов -> включить админские права для IDEA

@Controller
@RequiredArgsConstructor
@RequestMapping("file")
public class FileController {
    private final FileLocationService fileLocationService;

    //TODO: async подгрузка и загрузка, связка с версией

    @PostMapping("/upload")
    @ResponseBody
    FileDto upload(@RequestParam MultipartFile file) throws Exception {
        String fileType = FilenameUtils.getExtension(file.getOriginalFilename());
        return fileLocationService.save(file.getBytes(), file.getOriginalFilename(), fileType);
    }

    @GetMapping(value = "/download/{fileId}", produces = MediaType.ALL_VALUE)
    @ResponseBody
    FileSystemResource download(@PathVariable Long fileId){
        return fileLocationService.get(fileId);
    }

    @DeleteMapping(value = "/delete/{fileId}")
    @ResponseBody
    void delete(@PathVariable Long fileId) {
        fileLocationService.delete(fileId);
    }
}





