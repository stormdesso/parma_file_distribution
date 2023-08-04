package ru.parma.filesdistr.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.parma.filesdistr.dto.FolderDto;
import ru.parma.filesdistr.mappers.FolderMapper;
import ru.parma.filesdistr.models.Folder;
import ru.parma.filesdistr.models.Scope;
import ru.parma.filesdistr.repos.FileSystemRepository;
import ru.parma.filesdistr.repos.FolderRepository;
import ru.parma.filesdistr.repos.ScopeRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FolderService {
    private final FolderRepository folderRepository;

    private final ScopeRepository scopeRepository;

    private final FileSystemRepository fileSystemRepository;

    public List<FolderDto> getAll(long scope_id) {
        Scope scope = scopeRepository.getReferenceById(scope_id);
        if (scope == null) {
            throw new EntityNotFoundException(String.format("Пространства с id %d не существует", scope_id));
        }
        return FolderMapper.INSTANCE.toFolderDtos(scope.getFolders());
    }

    public FolderDto get(long folder_id) {
        Folder folder = folderRepository.getReferenceById(folder_id);
        if (folder == null) {
            throw new EntityNotFoundException( String.format("Папки с id %d не существует", folder_id));
        }
        return FolderMapper.INSTANCE.toFolderDto(folder);
    }

    public void add(FolderDto folderDto, long scope_id) {
        checkDto(folderDto);
        Scope scope = scopeRepository.getReferenceById(scope_id);
        if (scope == null) {
            throw new EntityNotFoundException(String.format("Пространства с id %d не существует", scope_id));
        }
        Folder folder = FolderMapper.INSTANCE.toFolder(folderDto);
        List<Folder> folders = scope.getFolders();
        folders.add(folder);
        folder.setScope(scope);
        folderRepository.save(folder);
    }

    public void update(FolderDto folderDto) {
        checkDto(folderDto);
        Folder existedFolder = folderRepository.getReferenceById(folderDto.getId());
        if (existedFolder == null) {
            throw new EntityNotFoundException("Такой папки для обновления не существует");
        }
        Folder folder = FolderMapper.INSTANCE.toFolder(folderDto);
        folderRepository.save(folder);
    }

    private void checkDto(FolderDto folderDto) {
        if (folderDto == null) {
            throw new IllegalArgumentException("Создаваемый объект не может быть null");
        }
        if (folderDto.getIdentifier() == null) {
            throw new IllegalArgumentException("Идентификатор не может быть null");
        }
        if (folderDto.getName() == null) {
            throw new IllegalArgumentException("Имя папки не может быть null");
        }
    }

    public void delete(Long folder_id) {
        Folder folder = folderRepository.getReferenceById(folder_id);
        if (folder==null) {
            throw new EntityNotFoundException( String.format("Папки с id %d не существует", folder_id));
        }
        fileSystemRepository.delete(folder.getRootPath());
        folderRepository.delete(folder);
    }
}
