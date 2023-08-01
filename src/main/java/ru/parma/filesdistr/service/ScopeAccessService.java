package ru.parma.filesdistr.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.parma.filesdistr.enums.TypeInScopePage;
import ru.parma.filesdistr.models.Folder;
import ru.parma.filesdistr.models.Scope;
import ru.parma.filesdistr.models.User;
import ru.parma.filesdistr.models.Version;
import ru.parma.filesdistr.repos.FolderRepository;
import ru.parma.filesdistr.repos.ScopeRepository;
import ru.parma.filesdistr.repos.UserRepository;
import ru.parma.filesdistr.repos.VersionRepository;

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
    public boolean tryGetAccess ( TypeInScopePage typeInScopePage, Integer generalId, @NotNull Integer userId){

        if(isAdminOrRoot())
            return true;

        User user = userRepository.getReferenceById(userId.longValue());

        if( typeInScopePage == TypeInScopePage.SCOPE ){
            Scope scope = scopeRepository.getReferenceById(generalId.longValue());
            return getAccess(scope, user);
        }
        else if( typeInScopePage == TypeInScopePage.FOLDER ){
            Folder folder = folderRepository.getReferenceById(generalId.longValue());
            return getAccess(folder.getScope(), user);
        }
        else if( typeInScopePage == TypeInScopePage.VERSION ){
            Version version = versionRepository.getReferenceById(Long.valueOf(generalId));
            return getAccess(version.getFolder().getScope(), user);
        }

        return false;
    }

    private boolean isAdminOrRoot(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROOT") || r.getAuthority().equals("ADMIN"));
    }

}
