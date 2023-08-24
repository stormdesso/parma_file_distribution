package ru.parma.filesdistr.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.parma.filesdistr.enums.Roles;
import ru.parma.filesdistr.enums.TypeInScopePage;
import ru.parma.filesdistr.mappers.UserMapper;
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
    private final ScopeRepository scopeRepository;
    private final FolderRepository folderRepository;
    private final VersionRepository versionRepository;
    private final UserRepository userRepository;
    private final CustomUserDetailsService customUserDetailsService;
    private final UserService userService;

    private boolean getAccess ( Scope scope, @NotNull User user){
        return user.getAvailableScopes().contains(scope);
    }

    public void tryGetAccessByUserId (TypeInScopePage typeInScopePage, Long generalId, @NotNull Long userId) throws AccessDeniedException {
        Scope scope = null;
        if(typeInScopePage == TypeInScopePage.SCOPE){
            Optional<Scope> scopeOptional = scopeRepository.findById(generalId);
            if (!scopeOptional.isPresent()) {
                throw new EntityNotFoundException(String.format("Scope с id %d  не найден", generalId));
            }
            scope = scopeOptional.get();
        }else if(typeInScopePage == TypeInScopePage.FOLDER){
            Optional<Folder> folderOptional = folderRepository.findById(generalId);
            if (!folderOptional.isPresent()) {
                throw new EntityNotFoundException(String.format("Folder с id %d  не найден", generalId));
            }
            scope = folderOptional.get().getScope();
        }
        else if(typeInScopePage == TypeInScopePage.VERSION){
            Optional<Version> versionOptional = versionRepository.findById(generalId);
            if (!versionOptional.isPresent()) {
                throw new EntityNotFoundException(String.format("Version с id %d  не найден", generalId));
            }
            scope = versionOptional.get().getFolder().getScope();
        }
        if (scope.isPermitAll()) return;

        if(isAdminOrRoot()) return ;
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            throw new EntityNotFoundException(String.format("User с id %d  не найден", userId));
        }
        User user = userOptional.get();
        boolean access = getAccess(scope, user);
        if(!access) {
            throw new AccessDeniedException("Access denied");
        }
    }

    private boolean isAdminOrRoot(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals(Roles.ROOT.toString()) || r.getAuthority().equals(Roles.ADMIN.toString()));
    }

    public void tryGetAccessToScopeForUser(TypeInScopePage typeInScopePage, Long generalId) throws AccessDeniedException{
        tryGetAccessByUserId (typeInScopePage, generalId, customUserDetailsService.getAuthorizedUser().getId());
    }

    private boolean isAdminScopes(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals(Roles.ADMIN_SCOPES.toString()));
    }

    public void canCreateAndDeleteScopes (boolean createNewScope) throws AccessDeniedException{
        if(isAdminOrRoot()){
            return;
        }
        if(isAdminScopes()){
            User user = customUserDetailsService.getAuthorizedUser();
            if (user.isCanCreateAndDeleteScope ()){
                if(createNewScope){
                    userService.checkMaxNumberOfScopes (UserMapper.INSTANCE.toAdminScopeDto(user));
                }
                return;
            }
        }
        throw new AccessDeniedException ("нет доступа");
    }

    public void canCreateFolderIn (@NotNull Scope scope) throws AccessDeniedException{
        tryGetAccessByUserId (TypeInScopePage.SCOPE, scope.getId (),
                CustomUserDetailsService.getAuthorizedUserId ());

        User user = customUserDetailsService.getAuthorizedUser ();
        userService.checkMaxNumberOfFolders (user.getMaxNumberFolder (), (long) scope.getFolders ().size ());
    }

}
