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

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScopeService {
    private final ScopeRepository scopeRepository;
    private final FileSystemRepository
    fileSystemRepository;

    public List<ScopeDto> getAll() {
        List<Scope> scopes = scopeRepository.findAll();
        deleteVersionFromFolder(scopes);
        return ScopeMapper.INSTANCE.toScopeDtos(scopes);
    }

    // TODO шляпа какая-то
    private void deleteVersionFromFolder ( @NotNull List<Scope> scopes){
        for (Scope scope: scopes) {
            for (Folder folder: scope.getFolders()) {
                folder.setVersions(null);
            }
        }
    }

    public void delete(long id) {
        Scope scope = scopeRepository.getReferenceById(id);
        fileSystemRepository.delete(scope.getRootPath());
    }

    public ScopeDto getScopeById(Long scopeId) {
        return ScopeMapper.INSTANCE.toScopeDto(scopeRepository.getReferenceById(scopeId));
    }
}
