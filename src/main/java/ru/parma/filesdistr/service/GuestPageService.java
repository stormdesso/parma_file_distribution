package ru.parma.filesdistr.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.parma.filesdistr.dto.GuestPageDto;
import ru.parma.filesdistr.mappers.FolderMapper;
import ru.parma.filesdistr.models.Scope;
import ru.parma.filesdistr.models.Version;
import ru.parma.filesdistr.repos.ScopeRepository;
import ru.parma.filesdistr.repos.VersionRepository;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GuestPageService {

    private final ScopeRepository scopeRepository;


    public GuestPageDto getGuestPageByScopeId(Long scopeId) {
        Scope scope = scopeRepository.getScopeByScopeIdAndLatestVersion(scopeId);
        if (scope==null) {
            throw new EntityNotFoundException("Scope с таким id не найден");
        }
        return convertEntityToDto(scope);
    }

    public GuestPageDto getGuestPageByVersionId(Long versionId) {
        Scope scope = scopeRepository.getScopeByVersionId(versionId);
        if (scope==null) {
            throw new EntityNotFoundException("Scope с такой версией не найден");
        }
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
