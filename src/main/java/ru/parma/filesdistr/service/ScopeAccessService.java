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

import java.nio.file.AccessDeniedException;

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

        User user = userRepository.findById(userId);

        boolean access = false;
        if( typeInScopePage == TypeInScopePage.SCOPE ){
            Scope scope = scopeRepository.findById(generalId);
            if (scope.isPermitAll()) return;
            access = getAccess(scope, user);
        }
        else if( typeInScopePage == TypeInScopePage.FOLDER ){
            Folder folder = folderRepository.findById(generalId);
            if (folder.getScope().isPermitAll()) return;
            access = getAccess(folder.getScope(), user);
        }
        else if( typeInScopePage == TypeInScopePage.VERSION ){
            Version version = versionRepository.findById(generalId);
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
