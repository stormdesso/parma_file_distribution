package ru.parma.filesdistr.controllers;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.parma.filesdistr.dto.FileDto;
import ru.parma.filesdistr.enums.MediaTypeInScopePage;
import ru.parma.filesdistr.enums.TypeInScopePage;
import ru.parma.filesdistr.service.CustomUserDetailsService;
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

    //TODO: async подгрузка и загрузка

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    FileDto upload (@RequestParam @NotNull MultipartFile file,
                    @RequestParam Long generalId,   //указывает id пространства, папки, версии
                    @RequestParam TypeInScopePage typeInScopePage,
                    @RequestParam MediaTypeInScopePage mediaTypeInScopePage,
                    @RequestParam(required = false) @Nullable Long tagId,
                    @RequestParam(required = false) @Nullable String comment) throws Exception {
        String fileType = FilenameUtils.getExtension(file.getOriginalFilename());

        scopeAccessService.tryGetAccess(typeInScopePage, generalId, CustomUserDetailsService.getAuthorizedUserId());

        return fileLocationService.save(file.getBytes(), file.getOriginalFilename(), fileType,
                generalId, typeInScopePage, mediaTypeInScopePage, tagId, comment);
    }

    @GetMapping(value = "/download/{fileId}", produces = MediaType.ALL_VALUE)
    @ResponseBody
    FileSystemResource download (@PathVariable Long fileId,
                                 @RequestParam Long generalId,   //указывает id пространства, папки, версии
                                 @RequestParam TypeInScopePage typeInScopePage) throws AccessDeniedException {
        scopeAccessService.tryGetAccess(typeInScopePage, generalId, CustomUserDetailsService.getAuthorizedUserId());
        return fileLocationService.get(fileId);
    }

    @DeleteMapping(value = "/delete/{fileId}")
    @ResponseBody
    void delete (@PathVariable Long fileId,
                 @RequestParam Long generalId,   //указывает id пространства, папки, версии
                 @RequestParam TypeInScopePage typeInScopePage) throws AccessDeniedException {
        scopeAccessService.tryGetAccess(typeInScopePage, generalId, CustomUserDetailsService.getAuthorizedUserId());
        fileLocationService.delete(fileId);
    }
}





