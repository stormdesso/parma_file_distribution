package ru.parma.filesdistr.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import ru.parma.filesdistr.dto.ScopeDto;
import ru.parma.filesdistr.mappers.ScopeMapper;
import ru.parma.filesdistr.models.Folder;
import ru.parma.filesdistr.models.Scope;
import ru.parma.filesdistr.repos.FileSystemRepository;
import ru.parma.filesdistr.repos.ScopeRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScopeService {
    private final ScopeRepository scopeRepository;
    private final FileSystemRepository fileSystemRepository;

    public List<ScopeDto> getAll() {
        List<Scope> scopes = scopeRepository.findAll();
        deleteVersionFromFolder(scopes);
        return ScopeMapper.INSTANCE.toScopeDtos(scopes);
    }

    public void add(ScopeDto scopeDto) {
        checkDto(scopeDto);
        Scope scope = ScopeMapper.INSTANCE.toScope(scopeDto);
        scopeRepository.save(scope);
    }

    public void update(ScopeDto scopeDto) {
        checkDto(scopeDto);
        Scope existedScope = scopeRepository.getReferenceById(scopeDto.getId());
        if (existedScope == null) {
            throw new EntityNotFoundException("Такого пространства для обновления не существует");
        }
        Scope scope = ScopeMapper.INSTANCE.toScope(scopeDto);
        scopeRepository.save(scope);
    }

    private void checkDto(ScopeDto scopeDto) {
        if (scopeDto == null) {
            throw new IllegalArgumentException("Создаваемое простарнство не может быть null");
        }
        if (scopeDto.getName() == null) {
            throw new IllegalArgumentException("Имя пространства не может быть null");
        }
    }

    private void deleteVersionFromFolder ( @NotNull List<Scope> scopes){
        for (Scope scope: scopes) {
            for (Folder folder: scope.getFolders()) {
                folder.setVersions(null);
            }
        }
    }

    public void delete(long id) {
        Scope scope = scopeRepository.getReferenceById(id);
        if (scope == null) {
            throw new EntityNotFoundException(String.format("Пространства с id %d не существует", id));
        }
        fileSystemRepository.delete(scope.getRootPath());
        scopeRepository.delete(scope);
    }

    public ScopeDto getScopeById(Long scopeId) {
        return ScopeMapper.INSTANCE.toScopeDto(scopeRepository.getReferenceById(scopeId));
    }
}
