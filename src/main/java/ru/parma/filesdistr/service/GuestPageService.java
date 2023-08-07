package ru.parma.filesdistr.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ru.parma.filesdistr.dto.GuestPageDto;
import ru.parma.filesdistr.enums.TypeInScopePage;
import ru.parma.filesdistr.mappers.ScopeMapper;
import ru.parma.filesdistr.models.Folder;
import ru.parma.filesdistr.models.Scope;
import ru.parma.filesdistr.models.Version;
import ru.parma.filesdistr.repos.ScopeRepository;
import ru.parma.filesdistr.repos.VersionRepository;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GuestPageService {

    private final ScopeRepository scopeRepository;

    private final VersionRepository versionRepository;

    private final ScopeAccessService scopeAccessService;

    public GuestPageDto getGuestPageByScopeId(Long scopeId) throws IOException {
        Scope scope = scopeRepository.getReferenceById(scopeId);
        if (scope==null) {
            throw new EntityNotFoundException(String.format("Scope с id %d  не найден", scopeId));
        }
        if (!scope.isPermitAll()) {
            if (CustomUserDetailsService.isUserHasRole()) {
                scopeAccessService.tryGetAccess(TypeInScopePage.SCOPE, scopeId, CustomUserDetailsService.getAuthorizedUserId());
            }
            else {
                HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
                response.sendRedirect("/login");
            }
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

    public GuestPageDto getGuestPageByVersionId(Long versionId) throws IOException {
        Version versionWithId = versionRepository.getReferenceById(versionId);
        if (versionWithId==null) {
            throw new EntityNotFoundException(String.format("Version с id %d  не найден", versionId));
        }
        String versionNumber = versionWithId.getVersionNumber();
        Folder folderWithVersion = versionWithId.getFolder();
        Scope scope = folderWithVersion.getScope();

        if (!scope.isPermitAll()) {
            if (CustomUserDetailsService.isUserHasRole()) {
                scopeAccessService.tryGetAccess(TypeInScopePage.VERSION, versionId,  CustomUserDetailsService.getAuthorizedUserId());
            }
            else {
                HttpServletResponse response = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
                response.sendRedirect("/login");
            }
        }

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

        return guestPageDto;
    }

}
