package ru.parma.filesdistr.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.parma.filesdistr.dto.GuestPageDto;
import ru.parma.filesdistr.mappers.ScopeMapper;
import ru.parma.filesdistr.models.Folder;
import ru.parma.filesdistr.models.Scope;
import ru.parma.filesdistr.models.Version;
import ru.parma.filesdistr.repos.ScopeRepository;
import ru.parma.filesdistr.repos.VersionRepository;

import javax.persistence.EntityNotFoundException;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GuestPageService {

    private final ScopeRepository scopeRepository;

    private final VersionRepository versionRepository;

    public GuestPageDto getGuestPageByScopeId(Long scopeId) {
        Scope scope = scopeRepository.getReferenceById(scopeId);
        if (scope==null) {
            throw new EntityNotFoundException("Scope с таким id не найден");
        }
        List<Folder> folders = scope.getFolders();
        for (Folder folder : folders) {
            List<Version> versions = folder.getVersions();
            Version versionWithMaxId = versions.stream().max(Comparator.comparing(v -> v.getId())).get();
            versions.clear();
            versions.add(versionWithMaxId);
        }
        return convertEntityToDto(scope);
    }

    public GuestPageDto getGuestPageByVersionId(Long versionId) {
        Version versionWithId = versionRepository.getReferenceById(versionId);
        if (versionWithId==null) {
            throw new EntityNotFoundException("Version с таким id не найден");
        }
        String versionNumber = versionWithId.getVersionNumber();
        Folder folderWithVersion = versionWithId.getFolder();
        Scope scope = folderWithVersion.getScope();
        List<Folder> folders = scope.getFolders();
        for (Folder folder : folders) {
            List<Version> versions = folder.getVersions();
            Version version = null;
            for (Version tmp : versions) {
                if (tmp.getVersionNumber().equals(versionNumber)) {
                    version = tmp;
                }
            }
            versions.clear();
            versions.add(version);
        }
        return convertEntityToDto(scope);
    }

    private GuestPageDto convertEntityToDto(Scope scope) {
        GuestPageDto guestPageDto = new GuestPageDto();

       guestPageDto.setScopeDto(ScopeMapper.INSTANCE.toScopeDto(scope));

        //TODO !!!не забыть добавить, если необходимо!!!
        return guestPageDto;
    }

}
