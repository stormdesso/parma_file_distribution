package ru.parma.filesdistr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.parma.filesdistr.dto.FolderDto;
import ru.parma.filesdistr.dto.VersionDto;
import ru.parma.filesdistr.models.Folder;
import ru.parma.filesdistr.models.Version;
import ru.parma.filesdistr.repos.FolderRepository;
import ru.parma.filesdistr.repos.VersionRepository;

@Service
public class VersionService {

    @Autowired
    VersionRepository versionRepository;

    private VersionDto convertEntityToDto(Version version){
        VersionDto versionDto = new VersionDto();

        return versionDto;
    }

}
