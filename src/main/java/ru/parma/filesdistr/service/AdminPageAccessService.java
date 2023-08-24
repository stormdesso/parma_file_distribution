package ru.parma.filesdistr.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import ru.parma.filesdistr.aop.annotations.LoggableMethod;
import ru.parma.filesdistr.aop.exceptions.AccessDeniedException;
import ru.parma.filesdistr.enums.Roles;
import ru.parma.filesdistr.models.User;
import ru.parma.filesdistr.repos.UserRepository;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AdminPageAccessService{
    private final UserRepository userRepository;
    private final CustomUserDetailsService customUserDetailsService;

    @LoggableMethod
    public void tryGetAccess(Long updatedUserId){
        User updatedUser = userRepository.findUserWithoutRoot(updatedUserId);

        if(isAdmin(updatedUser)){
            canEditAdmin ();
            return;
        }
        else if(updatedUser.getRoles ().contains (Roles.ADMIN_SCOPES)){
            canEditAdminScopes ();
            return;
        }
        throw new AccessDeniedException("нет доступа");
    }

    @LoggableMethod
    public void canEditAdmin (){
        User currUser = customUserDetailsService.getAuthorizedUser ();
        if(isRoot(currUser)){
            return;
        }
        if(isAdmin(currUser) & currUser.isAdminManager ()){
            return;
        }

        throw new AccessDeniedException ("нет доступа");
    }

    @LoggableMethod
    public void canEditAdminScopes (){
        User currUser = customUserDetailsService.getAuthorizedUser ();
        if(isRoot(currUser)){
            return;
        }
        if(isAdmin(currUser) & currUser.isAdminScopeManager ()){
            return;
        }

        throw new AccessDeniedException ("нет доступа");
    }

    public boolean isRoot (@NotNull User currUser){
        Set<Roles> rolesSet = currUser.getRoles ();
        return rolesSet.contains(Roles.ROOT);
    }

    public boolean isAdmin (@NotNull User currUser){
        Set<Roles> rolesSet = currUser.getRoles ();
        return rolesSet.contains(Roles.ADMIN);
    }
}
