package ru.parma.filesdistr.service;

import org.springframework.stereotype.Service;
import ru.parma.filesdistr.dto.FolderDto;
import ru.parma.filesdistr.models.Folder;
import ru.parma.filesdistr.repos.FolderRepository;

@Service
public class FolderService {

    final FolderRepository folderRepository;

    public FolderService(FolderRepository folderRepository) {
        this.folderRepository = folderRepository;
    }

    private FolderDto convertEntityToDto(Folder folder){
        FolderDto folderDto = new FolderDto();

        return folderDto;
    }

}
