package ru.parma.filesdistr.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.parma.filesdistr.enums.Roles;
import ru.parma.filesdistr.enums.TypeInScopePage;
import ru.parma.filesdistr.models.Folder;
import ru.parma.filesdistr.models.Scope;
import ru.parma.filesdistr.models.User;
import ru.parma.filesdistr.models.Version;
import ru.parma.filesdistr.repos.FolderRepository;
import ru.parma.filesdistr.repos.ScopeRepository;
import ru.parma.filesdistr.repos.UserRepository;
import ru.parma.filesdistr.repos.VersionRepository;

import javax.persistence.EntityNotFoundException;
import java.nio.file.AccessDeniedException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScopeAccessService {
    final ScopeRepository scopeRepository;
    final FolderRepository folderRepository;
    final VersionRepository versionRepository;
    final UserRepository userRepository;

    private boolean getAccess ( Scope scope, @NotNull User user){
        return user.getAvailableScopes().contains(scope);
    }

    public void tryGetAccess ( TypeInScopePage typeInScopePage, Long generalId, @NotNull Long userId) throws AccessDeniedException {

        if(isAdminOrRoot()) return ;

        Optional<User> userOptional = userRepository.findById(userId);

        if (!userOptional.isPresent()) {
            throw new EntityNotFoundException(String.format("User с id %d  не найден", userId));
        }
        User user = userOptional.get();
        boolean access = false;
        if( typeInScopePage == TypeInScopePage.SCOPE ){
            Optional<Scope> scopeOptional = scopeRepository.findById(generalId);
            if (!scopeOptional.isPresent()) {
                throw new EntityNotFoundException(String.format("Scope с id %d  не найден", generalId));
            }
            Scope scope = scopeOptional.get();
            if (scope.isPermitAll()) return;
            access = getAccess(scope, user);
        }
        else if( typeInScopePage == TypeInScopePage.FOLDER ){
            Optional<Folder> folderOptional = folderRepository.findById(generalId);
            if (!folderOptional.isPresent()) {
                throw new EntityNotFoundException(String.format("Folder с id %d  не найден", generalId));
            }
            Folder folder = folderOptional.get();
            if (folder.getScope().isPermitAll()) return;
            access = getAccess(folder.getScope(), user);
        }
        else if( typeInScopePage == TypeInScopePage.VERSION ){
            Optional<Version> versionOptional = versionRepository.findById(generalId);
            if (!versionOptional.isPresent()) {
                throw new EntityNotFoundException(String.format("Version с id %d  не найден", generalId));
            }
            Version version = versionOptional.get();
            if (version.getFolder().getScope().isPermitAll()) return;
            access = getAccess(version.getFolder().getScope(), user);
        }
        if(!access) {
            throw new AccessDeniedException("Access denied");
        }
    }

    private boolean isAdminOrRoot(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals(Roles.ROOT.toString()) || r.getAuthority().equals(Roles.ADMIN.toString()));
    }
}
