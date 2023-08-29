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
import ru.parma.filesdistr.enums.MediaTypeInAdminPage;
import ru.parma.filesdistr.enums.MediaTypeInScopePage;
import ru.parma.filesdistr.enums.TypeInScopePage;
import ru.parma.filesdistr.models.Version;
import ru.parma.filesdistr.service.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.AccessDeniedException;

//если не работает запись файлов -> включить админские права для IDEA

@Controller
@RequiredArgsConstructor
@RequestMapping("file")
public class FileController {
    private final HttpServletResponse response;

    private final FileLocationService fileLocationService;
    private final ScopeAccessService scopeAccessService;
    private final AdminPageAccessService adminPageAccessService;
    private final VersionService versionService;

    //TODO: async подгрузка и загрузка
    //TODO: return byte[]
    @PostMapping(value = "/scopes_page/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    FileDto uploadOnScopesPage (@RequestParam @NotNull MultipartFile file,
                                @RequestParam Long generalId,   //указывает id пространства, папки, версии
                                @RequestParam TypeInScopePage typeInScopePage,
                                @RequestParam MediaTypeInScopePage mediaTypeInScopePage,
                                @RequestParam(required = false) @Nullable Long tagId,
                                @RequestParam(required = false) @Nullable String comment) throws Exception {
        String fileType = FilenameUtils.getExtension(file.getOriginalFilename());

        scopeAccessService.tryGetAccessByUserId (typeInScopePage, generalId, CustomUserDetailsService.getAuthorizedUserId());

        return fileLocationService.save (file.getBytes(), file.getOriginalFilename(), fileType,
                generalId, typeInScopePage, mediaTypeInScopePage, tagId, comment);
    }


    @PostMapping(value = "/admin_page/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    FileDto uploadOnAdminPage (@RequestParam @NotNull MultipartFile file,
                               @RequestParam Long updatedUserId,
                               @RequestParam MediaTypeInAdminPage mediaTypeInAdminPage) throws Exception {
        String fileType = FilenameUtils.getExtension(file.getOriginalFilename());
        adminPageAccessService.tryGetAccess (updatedUserId);
        return fileLocationService.save (updatedUserId, file, mediaTypeInAdminPage, fileType);
    }


    @GetMapping(value = "/download/{fileId}", produces = MediaType.ALL_VALUE)
    @ResponseBody
    FileSystemResource download (@PathVariable Long fileId,
                                 @RequestParam Long generalId,   //указывает id пространства, папки, версии
                                 @RequestParam TypeInScopePage typeInScopePage) throws AccessDeniedException {

        scopeAccessService.tryGetAccessToScope ( typeInScopePage,  generalId);
        return fileLocationService.get(fileId);
    }


    @GetMapping(value="/download/{versionId}/zip", produces = "application/zip")
    @ResponseBody
    public byte[] downloadZIP(@PathVariable Long versionId) throws IOException{
        scopeAccessService.tryGetAccessToScope ( TypeInScopePage.VERSION,  versionId);

        Version version = versionService.get (versionId);
        response.addHeader("Content-Disposition",
                "attachment; filename=\""+version.getVersionNumber()+".zip\"");

        return fileLocationService.getZipArchive (fileLocationService.getFiles(versionId));
    }


    @GetMapping(value = "/download/byte/{fileId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    byte[] downloadAsByte (@PathVariable Long fileId,
                           @RequestParam Long generalId,   //указывает id пространства, папки, версии
                           @RequestParam TypeInScopePage typeInScopePage) throws IOException {

        scopeAccessService.tryGetAccessToScope ( typeInScopePage,  generalId);
        return fileLocationService.getAsByteArray(fileId);

    }

    @DeleteMapping(value = "/delete/{fileId}")
    @ResponseBody
    void delete (@PathVariable Long fileId,
                 @RequestParam Long generalId,   //указывает id пространства, папки, версии
                 @RequestParam TypeInScopePage typeInScopePage) throws AccessDeniedException {
        scopeAccessService.tryGetAccessByUserId (typeInScopePage, generalId, CustomUserDetailsService.getAuthorizedUserId());
        fileLocationService.delete(fileId);
    }
}