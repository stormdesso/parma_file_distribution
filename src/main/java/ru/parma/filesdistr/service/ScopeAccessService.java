package ru.parma.filesdistr.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.parma.filesdistr.aop.annotations.LoggableMethod;
import ru.parma.filesdistr.aop.exceptions.AccessDeniedException;
import ru.parma.filesdistr.aop.exceptions.EntityNotFoundException;
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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScopeAccessService {
    private final ScopeRepository scopeRepository;
    private final FolderRepository folderRepository;
    private final VersionRepository versionRepository;
    private final UserRepository userRepository;
    private final ScopeService scopeService;
    private final CustomUserDetailsService customUserDetailsService;

    private boolean getAccess ( Scope scope, @NotNull User user){
        return user.getAvailableScopes().contains(scope);
    }

    @LoggableMethod
    public void tryGetAccessByUserId (TypeInScopePage typeInScopePage, Long generalId, @NotNull Long userId) throws AccessDeniedException {

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

    private boolean isAdminOrRoot() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals(Roles.ROOT.toString()) || r.getAuthority().equals(Roles.ADMIN.toString()));
    }

    @LoggableMethod
    public void tryGetAccessToScope(TypeInScopePage typeInScopePage, Long generalId) throws AccessDeniedException{
        Scope scope = scopeService.getScopeBy(typeInScopePage, generalId);

        if(!scope.isPermitAll ()){
            tryGetAccessByUserId (typeInScopePage, generalId,
                    customUserDetailsService.getAuthorizedUser().getId ());//кидает ошибку,
            // если нет доступа
        }
    }

}
