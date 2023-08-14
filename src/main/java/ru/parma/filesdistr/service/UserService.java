package ru.parma.filesdistr.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.parma.filesdistr.dto.AdminDto;
import ru.parma.filesdistr.dto.AdminScopeDto;
import ru.parma.filesdistr.enums.Roles;
import ru.parma.filesdistr.mappers.ScopeMapper;
import ru.parma.filesdistr.mappers.UserMapper;
import ru.parma.filesdistr.models.User;
import ru.parma.filesdistr.repos.ScopeRepository;
import ru.parma.filesdistr.repos.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private  String encryptPassword (String password){
        BCryptPasswordEncoder tmp = new BCryptPasswordEncoder (8);
        return tmp.encode (password);
    }
    private final UserRepository userRepository;
    private final ScopeRepository scopeRepository;


    private @NotNull User getAuthorizedUser(){

        Long id = CustomUserDetailsService.getAuthorizedUserId ();
        Optional <User> optUser = userRepository.findById(id);
        if(!optUser.isPresent ()){
            throw new EntityNotFoundException (String.format("User с id %d  не найден", id));
        }

        return optUser.get();
    }

    private @NotNull User getUserById(Long id){
        Optional <User> optUser = userRepository.findById(id);
        if(!optUser.isPresent ()){
            throw new EntityNotFoundException (String.format("User с id %d  не найден", id));
        }

        return optUser.get();
    }


    private boolean canEditAdmin () {
        User currUser = getAuthorizedUser();
        Set <Roles> rolesSet = currUser.getRoles ();
        if(rolesSet.containsAll (new HashSet <Roles> () {{
            add (Roles.ROOT);
        }})){
            return true;
        }
        if(rolesSet.containsAll (new HashSet <Roles> () {{
            add (Roles.ADMIN);
        }}) & currUser.isAdminManager ()){
            return true;
        }

        return false;
    }

    private boolean canEditAdminScopes () {
        User currUser = getAuthorizedUser();
        Set <Roles> rolesSet = currUser.getRoles ();
        if(rolesSet.containsAll (new HashSet <Roles> () {{
            add (Roles.ROOT);
        }})){
            return true;
        }
        if(rolesSet.containsAll (new HashSet <Roles> () {{
            add (Roles.ADMIN);
        }}) & currUser.isAdminScopeManager ()){
            return true;
        }

        return false;
    }


    public Set <AdminDto> getAllAdmins () {

        Set <User> users = new HashSet <> ();
        Long id = CustomUserDetailsService.getAuthorizedUserId ();
        if(canEditAdmin ()){
            users.addAll (userRepository.findByRolesContainingAndIdNot(Roles.ADMIN, id));
        }

        return UserMapper.INSTANCE.toAdminDtos (users);
    }

    public Set <AdminScopeDto> getAllAdminsScopes () {
        Set <User> users = new HashSet <> ();
        Long id = CustomUserDetailsService.getAuthorizedUserId ();
        if(canEditAdminScopes ()){
            users.addAll (userRepository.findByRolesContainingAndIdNot(Roles.ADMIN_SCOPES, id));
        }

        return UserMapper.INSTANCE.toAdminScopeDtos (users);
    }


    public void add (@NotNull AdminDto adminDto) {
        if(canEditAdmin ()){
            Set<Roles> roles = new HashSet<> ();
            roles.add (Roles.ADMIN);

            User user = User.builder ()
                    .name (adminDto.getName ())
                    .password (encryptPassword (adminDto.getPassword ()))
                    .blocked (adminDto.isBlocked ())
                    .isAdminManager (adminDto.isAdminManager ())
                    .isAdminScopeManager (adminDto.isAdminScopeManager ())
                    .canCreateAndDeleteScope (true)
                    .maxNumberScope (9999L)
                    .maxNumberFolder (9999L)
                    .maxStorageSpace (9999L)
                    .roles (roles)
                    .build ();
            userRepository.save (user);
        }
    }


    public void update (@NotNull AdminDto adminDto) {
        Long id = CustomUserDetailsService.getAuthorizedUserId ();
        User currUser = getUserById(id);
        User updatedUser = getUserById(adminDto.getId());

        if(currUser.getId () != updatedUser.getId () & canEditAdmin ()){
            UserMapper.INSTANCE.fromAdminDtoToUser (adminDto, updatedUser);
            userRepository.save (updatedUser);
        }
    }

    public void deleteAdmin (Long adminId) {
        if(canEditAdmin ())
            userRepository.deleteById (adminId);
    }



    public void add (@NotNull AdminScopeDto adminScopeDto) {
        if(canEditAdminScopes ()){
            User user = User.builder ()
                    .name (adminScopeDto.getName ())
                    .password (encryptPassword (adminScopeDto.getPassword ()))
                    .blocked (adminScopeDto.isBlocked ())
                    .isAdminManager (false)
                    .isAdminScopeManager (false)
                    .canCreateAndDeleteScope (adminScopeDto.isCanCreateAndDeleteScope ())
                    .maxNumberScope (adminScopeDto.getMaxNumberScope ())
                    .maxStorageSpace (adminScopeDto.getMaxStorageSpace ())
                    .maxNumberFolder (adminScopeDto.getMaxNumberFolder ())
                    .availableScopes (ScopeMapper.INSTANCE.toScope (scopeRepository, adminScopeDto.getScopePreviewDtos ()))
                    .roles (new HashSet <Roles> () {{
                        add (Roles.ADMIN_SCOPES);
                    }})
                    .build ();

            userRepository.save (user);
        }
    }

    public void update (@NotNull AdminScopeDto adminScopeDto) {
        Long id = CustomUserDetailsService.getAuthorizedUserId ();
        User currUser = getUserById(id);
        User updatableUser = getUserById(adminScopeDto.getId ());

        if(currUser.getId () != updatableUser.getId ()){
            if(canEditAdminScopes ()){
                UserMapper.INSTANCE.fromAdminScopeDtoToUser(adminScopeDto, updatableUser,
                        ScopeMapper.INSTANCE.toScope (scopeRepository, adminScopeDto.getScopePreviewDtos ()));
                userRepository.save (updatableUser);
            }
        }
    }

    public void deleteAdminScope (Long adminScopeId) {
        if(canEditAdminScopes ())
            userRepository.deleteById (adminScopeId);
    }

}
