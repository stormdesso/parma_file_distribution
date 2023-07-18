package ru.parma.filesdistr.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.parma.filesdistr.dto.FileDto;
import ru.parma.filesdistr.dto.FolderDto;
import ru.parma.filesdistr.dto.ScopeDto;
import ru.parma.filesdistr.dto.ViewScopeDto;
import ru.parma.filesdistr.models.Folder;
import ru.parma.filesdistr.models.Scope;
import ru.parma.filesdistr.models.Version;
import ru.parma.filesdistr.repos.ScopeRepository;
import ru.parma.filesdistr.repos.VersionRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ViewScopeService {

    private final ScopeRepository scopeRepository;

    private final VersionRepository versionRepository;

    public ViewScopeDto getViewScopeByScopeIdAndVersionId(Long scopeId, Long versionId) {
        Scope scope = scopeRepository.getReferenceById(scopeId);
        Version version = versionRepository.getReferenceById(versionId);
        return convertEntityToDto(scope, version);
    }

    private ViewScopeDto convertEntityToDto(Scope scope, Version version) {
        ViewScopeDto viewScopeDto = new ViewScopeDto();
        viewScopeDto.setVersionNumber(version.getVersionNumber());

        //TODO !!!не забыть заменить!!!
        viewScopeDto.setFolders(new ArrayList<>());

        //TODO !!!не забыть добавить, если необходимо!!!
        return viewScopeDto;
    }

}
