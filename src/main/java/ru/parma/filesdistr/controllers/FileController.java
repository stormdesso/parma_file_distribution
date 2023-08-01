package ru.parma.filesdistr.controllers;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.parma.filesdistr.dto.SavedFileDto;
import ru.parma.filesdistr.enums.MediaTypeInScopePage;
import ru.parma.filesdistr.enums.TypeInScopePage;
import ru.parma.filesdistr.service.FileLocationService;
import ru.parma.filesdistr.service.ScopeAccessService;

import java.nio.file.AccessDeniedException;

//если не работает запись файлов -> включить админские права для IDEA

@Controller
@RequiredArgsConstructor
@RequestMapping("file")
public class FileController {
    private final FileLocationService fileLocationService;
    private final ScopeAccessService scopeAccessService;

    //TODO: async подгрузка и загрузка, связка с версией
    //userId доставать из cookie

    //TODO: заменить
    static int userId = 2;//test

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    SavedFileDto upload ( @RequestParam @NotNull MultipartFile file,
                          @RequestParam Integer generalId,   //указывает id пространства, папки, версии
                          @RequestParam TypeInScopePage typeInScopePage,
                          @RequestParam MediaTypeInScopePage mediaTypeInScopePage) {
        String fileType = FilenameUtils.getExtension(file.getOriginalFilename());

        try {
            if(scopeAccessService.tryGetAccess(typeInScopePage, generalId, userId)){
                return fileLocationService.save(file.getBytes(), file.getOriginalFilename(), fileType,
                        generalId, typeInScopePage, mediaTypeInScopePage);
            }
            else throw new AccessDeniedException("Access denied");
        }
        catch (Exception e){

        }
        return null;
    }

    @GetMapping(value = "/download/{fileId}", produces = MediaType.ALL_VALUE)
    @ResponseBody
    FileSystemResource download ( @PathVariable Long fileId,
                                  @RequestParam Integer generalId,   //указывает id пространства, папки, версии
                                  @RequestParam TypeInScopePage typeInScopePage){
        try {
            if(scopeAccessService.tryGetAccess(typeInScopePage, generalId, userId)) {
                return fileLocationService.get(fileId);
            }
            else throw new AccessDeniedException("Access denied");

        }
        catch (Exception e){

        }
        return null;
    }

    @DeleteMapping(value = "/delete/{fileId}")
    @ResponseBody
    void delete ( @PathVariable Long fileId,
                  @RequestParam Integer generalId,   //указывает id пространства, папки, версии
                  @RequestParam TypeInScopePage typeInScopePage) {
        try {
            if(scopeAccessService.tryGetAccess(typeInScopePage, generalId, userId)) {
                fileLocationService.delete(fileId);
            }
            else throw new AccessDeniedException("Access denied");
        }
        catch (Exception e){

        }

    }
}





