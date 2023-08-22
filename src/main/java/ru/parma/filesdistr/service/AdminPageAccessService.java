package ru.parma.filesdistr.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import ru.parma.filesdistr.enums.Roles;
import ru.parma.filesdistr.models.User;
import ru.parma.filesdistr.repos.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AdminPageAccessService{
    private final UserRepository userRepository;
    private final CustomUserDetailsService customUserDetailsService;
    public @NotNull User getUserById (Long id){
        Optional<User> optUser = userRepository.findById(id);
        if(! optUser.isPresent ()){
            throw new EntityNotFoundException (String.format ("User с id: %d не найден", id));
        }
        User user =  optUser.get ();
        if(user.getRoles ().contains (Roles.ROOT) ){
            throw new AccessDeniedException ("Нет доступа");
        }
        return user;
    }


    public void tryGetAccess(Long updatedUserId){
        User updatedUser = getUserById (updatedUserId);

        if(updatedUser.getRoles ().contains (Roles.ADMIN)){
            canEditAdmin ();
            return;
        }
        else if(updatedUser.getRoles ().contains (Roles.ADMIN_SCOPES)){
            canEditAdminScopes ();
            return;
        }
        throw new AccessDeniedException ("нет доступа");
    }

    public void canEditAdmin (){
        User currUser = customUserDetailsService.getAuthorizedUser ();
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
        User currUser = customUserDetailsService.getAuthorizedUser ();
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
