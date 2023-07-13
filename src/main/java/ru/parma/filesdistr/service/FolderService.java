package ru.parma.filesdistr.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.parma.filesdistr.dto.FolderDto;
import ru.parma.filesdistr.models.Folder;
import ru.parma.filesdistr.repos.FolderRepository;

@Service
@RequiredArgsConstructor
public class FolderService {

    private final FolderRepository folderRepository;
    private FolderDto convertEntityToDto(Folder folder){
        FolderDto folderDto = new FolderDto();

        return folderDto;
    }

}
