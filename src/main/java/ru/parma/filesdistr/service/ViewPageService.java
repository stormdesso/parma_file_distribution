package ru.parma.filesdistr.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ru.parma.filesdistr.dto.ViewPageDto;
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
public class ViewPageService {

    private final ScopeRepository scopeRepository;

    private final VersionRepository versionRepository;

    private final ScopeAccessService scopeAccessService;

    public ViewPageDto getViewPage(Long scopeId, String versionId) throws IOException {
        Scope scope = scopeRepository.getReferenceById(scopeId);
        if (scope==null) {
            throw new EntityNotFoundException(String.format("Scope с id %d  не найден", scopeId));
        }

        checkAccessToScope(scope);

        if (versionId.equals("latest")) {
            List<Folder> folders = scope.getFolders();
            for (Folder folder : folders) {
                List<Version> versions = folder.getVersions();
                Version versionWithMaxId = versions.stream().max(Comparator.comparing(v -> v.getDateOfPublication().getTime())).get();
                versions.clear();
                versions.add(versionWithMaxId);
            }
        }

        else {
            long v = Long.parseLong(versionId);
            Version versionWithId = versionRepository.getReferenceById(v);
            if (versionWithId==null) {
                throw new EntityNotFoundException(String.format("Version с id %s  не найден", versionId));
            }
            String versionNumber = versionWithId.getVersionNumber();
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
        }
        return convertEntityToDto(scope);
    }

    private void checkAccessToScope(@NotNull Scope scope) throws IOException {
        if (!scope.isPermitAll()) {
            if (CustomUserDetailsService.tryGetAuthentication()) {
                scopeAccessService.tryGetAccess(TypeInScopePage.SCOPE, scope.getId(), CustomUserDetailsService.getAuthorizedUserId());
            }
            else {
                HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
                response.sendRedirect("/login");
            }
        }
    }

    private @NotNull ViewPageDto convertEntityToDto(Scope scope) {
        ViewPageDto viewPageDto = new ViewPageDto();

       viewPageDto.setScopeDto(ScopeMapper.INSTANCE.toScopeDto(scope));

        return viewPageDto;
    }

}
