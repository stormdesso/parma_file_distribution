package ru.parma.filesdistr.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import ru.parma.filesdistr.dto.ScopeDto;
import ru.parma.filesdistr.mappers.ScopeMapper;
import ru.parma.filesdistr.models.Folder;
import ru.parma.filesdistr.models.Scope;
import ru.parma.filesdistr.repos.ScopeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScopeService {
    private final ScopeRepository scopeRepository;

    public List<ScopeDto> getAll() {
        List<Scope> scopes = scopeRepository.findAll();

        //TODO:удалить перед коммитом
//        Version version = scopes.get(0).getFolders().get(0).getVersions().get(0);
//        String path = version.getRootPath();

        deleteVersionFromFolder(scopes);
        return ScopeMapper.INSTANCE.toScopeDtos(scopes);
    }
    private void deleteVersionFromFolder ( @NotNull List<Scope> scopes){
        for (Scope scope: scopes) {
            for (Folder folder: scope.getFolders()) {
                folder.setVersions(null);
            }
        }
    }

    public ScopeDto getScopeById(Long scopeId) {
        return ScopeMapper.INSTANCE.toScopeDto(scopeRepository.getReferenceById(scopeId));
    }
}
