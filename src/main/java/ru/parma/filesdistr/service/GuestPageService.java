package ru.parma.filesdistr.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.parma.filesdistr.dto.GuestPageDto;
import ru.parma.filesdistr.mappers.FolderMapper;
import ru.parma.filesdistr.models.Scope;
import ru.parma.filesdistr.models.Version;
import ru.parma.filesdistr.repos.ScopeRepository;
import ru.parma.filesdistr.repos.VersionRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GuestPageService {

    private final ScopeRepository scopeRepository;


    public GuestPageDto getGuestPageByScopeIdAndVersionId(Long scopeId, Long versionId) {
        Scope scope = scopeRepository.getScopeByScopeIdAndVersionId(scopeId, versionId);
        return convertEntityToDto(scope);
    }

    private GuestPageDto convertEntityToDto(Scope scope) {
        GuestPageDto guestPageDto = new GuestPageDto();

        //Переделка через mapper
        guestPageDto.setFolders(FolderMapper.INSTANCE.toFolderDtos(scope.getFolders()));

        //Задание версии
        Version version = scope.getFolders().get(0).getVersions().get(0);
        guestPageDto.setVersionNumber(version.getVersionNumber());

        //TODO !!!не забыть добавить, если необходимо!!!
        return guestPageDto;
    }

}
