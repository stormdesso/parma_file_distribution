package ru.parma.filesdistr.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.parma.filesdistr.dto.FileDto;
import ru.parma.filesdistr.dto.FolderDto;
import ru.parma.filesdistr.dto.GuestPageDto;
import ru.parma.filesdistr.dto.VersionDto;
import ru.parma.filesdistr.models.File;
import ru.parma.filesdistr.models.Folder;
import ru.parma.filesdistr.models.Scope;
import ru.parma.filesdistr.models.Version;
import ru.parma.filesdistr.repos.ScopeRepository;
import ru.parma.filesdistr.repos.VersionRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GuestPageService {

    private final ScopeRepository scopeRepository;

    private final VersionRepository versionRepository;



    public GuestPageDto getGuestPageByScopeIdAndVersionId(Long scopeId, Long versionId) {
        Scope scope = scopeRepository.getReferenceById(scopeId);
        Version version = versionRepository.getReferenceById(versionId);
        return convertEntityToDto(scope, version);
    }

    private GuestPageDto convertEntityToDto(Scope scope, Version version) {
        GuestPageDto guestPageDto = new GuestPageDto();

        //Задание версии
        guestPageDto.setVersionNumber(version.getVersionNumber());

        List<Folder> folders = scope.getFolders();
        //Первичная версия задания папок
        List<FolderDto> folderDtoList = new ArrayList<>();
        for (Folder folder : folders) {
            folderDtoList.add(convertFoldersToDto(folder, version));
        }
        guestPageDto.setFolders(folderDtoList);

        //TODO !!!не забыть добавить, если необходимо!!!
        return guestPageDto;
    }

    private FolderDto convertFoldersToDto(Folder folder, Version requiredVersion) {
        FolderDto folderDto = new FolderDto();
        folderDto.setId(folder.getId());
        folderDto.setName(folder.getName());

        //Первичная версия задания файлов
        List<VersionDto> versionDtoList = new ArrayList<>();
        List<Version> versions = folder.getVersions();
        if (versions.contains(requiredVersion)) {
            versionDtoList.add(convertVersionToDto(requiredVersion));
        }
        folderDto.setVersions(versionDtoList);
        return folderDto;
    }

    private VersionDto convertVersionToDto(Version version) {
        VersionDto versionDto = new VersionDto();

        versionDto.setVersionNumber(version.getVersionNumber());

        List<File> files = version.getFiles();
        List<FileDto> fileDtoList = new ArrayList<>();
        for (File file : files) {
            fileDtoList.add(convertFileToDto(file));
        }
        versionDto.setFiles(fileDtoList);

        //TODO !!!не забыть доработать!!!
        return versionDto;
    }

    private FileDto convertFileToDto(File file) {
        FileDto fileDto = new FileDto();
        fileDto.setId(file.getId());
        fileDto.setName(file.getName());
        fileDto.setType(file.getType());
        fileDto.setSize(file.getSize());
        return fileDto;
    }
}
