package ru.parma.filesdistr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.parma.filesdistr.dto.FileDto;
import ru.parma.filesdistr.dto.FolderDto;
import ru.parma.filesdistr.dto.ScopeDto;
import ru.parma.filesdistr.models.Folder;
import ru.parma.filesdistr.models.Scope;
import ru.parma.filesdistr.repos.FolderRepository;
import ru.parma.filesdistr.repos.LicenseAgreementFileForScopeRepository;
import ru.parma.filesdistr.repos.ScopeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FolderService {

    @Autowired
    FolderRepository folderRepository;

    private FolderDto convertEntityToDto(Folder folder){
        FolderDto folderDto = new FolderDto();

        return folderDto;
    }

}
