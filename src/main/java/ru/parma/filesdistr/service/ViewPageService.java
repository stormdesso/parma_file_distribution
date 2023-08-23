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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ViewPageService {

    private final ScopeRepository scopeRepository;

    private final VersionRepository versionRepository;

    private final ScopeAccessService scopeAccessService;

    public ViewPageDto getViewPage(Long scopeId, String versionId) throws IOException {
        Optional<Scope> scopeOpt = scopeRepository.findById(scopeId);
        if (!scopeOpt.isPresent()) {
            throw new EntityNotFoundException(String.format("Scope с id %d  не найден", scopeId));
        }
        Scope scope = scopeOpt.get();
        checkAccessToScope(scope);

        if (versionId.equalsIgnoreCase("latest")) {
            List<Folder> folders = scope.getFolders();
            for (Folder folder : folders) {
                List<Version> versions = folder.getVersions();
                if(!versions.isEmpty()) {
                    Version versionWithMaxId = versions.stream()
                            .max(Comparator.comparing(v -> v.getDateOfPublication().getTime()))
                            .get();
                    versions.clear();
                    versions.add(versionWithMaxId);
                }
            }
        } else {
            long vId = Long.parseLong(versionId);
            Optional<Version> versionByIdOpt = versionRepository.findById(vId);
            if (!versionByIdOpt.isPresent()) {
                throw new EntityNotFoundException(String.format("Version с id %s  не найден", versionId));
            }
            Version versionById = versionByIdOpt.get();
            String versionNumber = versionById.getVersionNumber();
            List<Folder> folders = scope.getFolders();
            for (Folder folder : folders) {
                List<Version> versions = folder.getVersions();
                Version version = null;
                for (Version v : versions) {
                    if (v.getVersionNumber().equals(versionNumber)) {
                        version = v;
                        break;
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
            if (CustomUserDetailsService.isAuthenticated()) {
                scopeAccessService.tryGetAccess (TypeInScopePage.SCOPE, scope.getId(), CustomUserDetailsService.getAuthorizedUserId());
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
