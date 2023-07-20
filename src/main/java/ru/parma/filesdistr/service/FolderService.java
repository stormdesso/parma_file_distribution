package ru.parma.filesdistr.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.parma.filesdistr.repos.FolderRepository;

@Service
@RequiredArgsConstructor
public class FolderService {
    private final FolderRepository folderRepository;
}
