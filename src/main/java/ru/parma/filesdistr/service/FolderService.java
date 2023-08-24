package ru.parma.filesdistr.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.parma.filesdistr.aop.annotations.LoggableMethod;
import ru.parma.filesdistr.aop.exceptions.EntityIllegalArgumentException;
import ru.parma.filesdistr.aop.exceptions.EntityNotFoundException;
import ru.parma.filesdistr.dto.FolderDto;
import ru.parma.filesdistr.mappers.FolderMapper;
import ru.parma.filesdistr.models.Folder;
import ru.parma.filesdistr.models.Scope;
import ru.parma.filesdistr.repos.FileSystemRepository;
import ru.parma.filesdistr.repos.FolderRepository;
import ru.parma.filesdistr.repos.ScopeRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FolderService {
    private final FolderRepository folderRepository;

    private final ScopeRepository scopeRepository;

    private final FileSystemRepository fileSystemRepository;

    @LoggableMethod
    public List<FolderDto> getAll(long scope_id) {
        Optional<Scope> scopeOptional = scopeRepository.findById(scope_id);
        if (!scopeOptional.isPresent()) {
            throw new EntityNotFoundException(String.format("Пространства с id %d не существует", scope_id));
        }
        return FolderMapper.INSTANCE.toFolderDtos(scopeOptional.get().getFolders());
    }

    public FolderDto getDto (long folderId) {
        return FolderMapper.INSTANCE.toFolderDto(get(folderId));
    }

    @LoggableMethod
    public Folder get (long folderId) {
        Optional<Folder> folderOptional = folderRepository.findById(folderId);
        if (!folderOptional.isPresent()) {
            throw new EntityNotFoundException( String.format("Папки с id %d не существует", folderId));
        }
        return folderOptional.get();
    }

    @LoggableMethod
    public void add(FolderDto folderDto, long scope_id) {
        checkDto(folderDto);
        Optional<Scope> scopeOptional = scopeRepository.findById(scope_id);
        if (!scopeOptional.isPresent()) {
            throw new EntityNotFoundException(String.format("Пространства с id %d не существует", scope_id));
        }
        Scope scope = scopeOptional.get();
        Folder folder = FolderMapper.INSTANCE.toFolder(folderDto);
        List<Folder> folders = scope.getFolders();
        folders.add(folder);
        folder.setScope(scope);
        folderRepository.save(folder);
    }

    @LoggableMethod
    public void update(FolderDto folderDto) {
        checkDto(folderDto);
        Optional<Folder> existedFolder = folderRepository.findById(folderDto.getId());
        if (!existedFolder.isPresent()) {
            throw new EntityNotFoundException("Такой папки для обновления не существует");
        }
        Folder folder = FolderMapper.INSTANCE.toFolder(folderDto);
        folderRepository.save(folder);
    }

    @LoggableMethod
    private void checkDto(FolderDto folderDto) {
        if (folderDto == null) {
            throw new EntityIllegalArgumentException("Создаваемый объект не может быть null");
        }
        if (folderDto.getIdentifier() == null) {
            throw new EntityIllegalArgumentException("Идентификатор не может быть null");
        }
        if (folderDto.getName() == null) {
            throw new EntityIllegalArgumentException("Имя папки не может быть null");
        }
    }

    @LoggableMethod
    public void delete(Long folder_id) {
        Optional<Folder> folderOptional = folderRepository.findById(folder_id);
        if (!folderOptional.isPresent()) {
            throw new EntityNotFoundException( String.format("Папки с id %d не существует", folder_id));
        }
        fileSystemRepository.delete(folderOptional.get().getRootPath());
        folderRepository.delete(folderOptional.get());
    }
}
