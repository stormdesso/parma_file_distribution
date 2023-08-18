package ru.parma.filesdistr.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import ru.parma.filesdistr.enums.Roles;
import ru.parma.filesdistr.models.User;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AdminPageAccessService{
    private final UserService userService;

    public void tryGetAccess(Long userId){
        User user = userService.getUserById (userId);

        if(user.getRoles ().contains (Roles.ADMIN)){
            canEditAdmin ();
        }
        else if(user.getRoles ().contains (Roles.ADMIN_SCOPES)){
            canEditAdminScopes ();
        }
        throw new AccessDeniedException ("нет доступа");
    }

    public void canEditAdmin (){
        User currUser = userService.getAuthorizedUser ();
        Set<Roles> rolesSet = currUser.getRoles ();
        if(rolesSet.containsAll (new HashSet<Roles> (){{
            add (Roles.ROOT);
        }})){
            return;
        }
        if(rolesSet.containsAll (new HashSet<Roles> (){{
            add (Roles.ADMIN);
        }}) & currUser.isAdminManager ()){
            return;
        }

        throw new AccessDeniedException ("нет доступа");
    }

    public void canEditAdminScopes (){
        User currUser = userService.getAuthorizedUser ();
        Set<Roles> rolesSet = currUser.getRoles ();
        if(rolesSet.containsAll (new HashSet<Roles> (){{
            add (Roles.ROOT);
        }})){
            return;
        }
        if(rolesSet.containsAll (new HashSet<Roles> (){{
            add (Roles.ADMIN);
        }}) & currUser.isAdminScopeManager ()){
            return;
        }

        throw new AccessDeniedException ("нет доступа");
    }

}
