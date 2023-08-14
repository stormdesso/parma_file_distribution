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
public class UserService{

    private String encryptPassword(String password){
        BCryptPasswordEncoder tmp = new BCryptPasswordEncoder (8);
        return tmp.encode (password);
    }

    private final UserRepository userRepository;
    private final ScopeRepository scopeRepository;
    private static final String USERNAME_REGEX = "^(?=.{8,20}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$";
    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";


    private @NotNull User getAuthorizedUser(){

        Long id = CustomUserDetailsService.getAuthorizedUserId ();
        Optional <User> optUser = userRepository.findById (id);
        if(! optUser.isPresent ()){
            throw new EntityNotFoundException (String.format ("User с id %d  не найден", id));
        }

        return optUser.get ();
    }

    private @NotNull User getUserById(Long id){
        Optional <User> optUser = userRepository.findById (id);
        if(! optUser.isPresent ()){
            throw new EntityNotFoundException (String.format ("User с id %d  не найден", id));
        }

        return optUser.get ();
    }


    private boolean canEditAdmin(){
        User currUser = getAuthorizedUser ();
        Set <Roles> rolesSet = currUser.getRoles ();
        if(rolesSet.containsAll (new HashSet <Roles> (){{
            add (Roles.ROOT);
        }})){
            return true;
        }
        if(rolesSet.containsAll (new HashSet <Roles> (){{
            add (Roles.ADMIN);
        }}) & currUser.isAdminManager ()){
            return true;
        }

        return false;
    }

    private boolean canEditAdminScopes(){
        User currUser = getAuthorizedUser ();
        Set <Roles> rolesSet = currUser.getRoles ();
        if(rolesSet.containsAll (new HashSet <Roles> (){{
            add (Roles.ROOT);
        }})){
            return true;
        }
        if(rolesSet.containsAll (new HashSet <Roles> (){{
            add (Roles.ADMIN);
        }}) & currUser.isAdminScopeManager ()){
            return true;
        }

        return false;
    }


    public Set <AdminDto> getAllAdmins(){

        Set <User> users = new HashSet <> ();
        Long id = CustomUserDetailsService.getAuthorizedUserId ();
        if(canEditAdmin ()){
            users.addAll (userRepository.findByRolesContainingAndIdNot (Roles.ADMIN, id));
        }

        return UserMapper.INSTANCE.toAdminDtos (users);
    }

    public Set <AdminScopeDto> getAllAdminsScopes(){
        Set <User> users = new HashSet <> ();
        Long id = CustomUserDetailsService.getAuthorizedUserId ();
        if(canEditAdminScopes ()){
            users.addAll (userRepository.findByRolesContainingAndIdNot (Roles.ADMIN_SCOPES, id));
        }

        return UserMapper.INSTANCE.toAdminScopeDtos (users);
    }


    private void matchesRegex(@NotNull User user){
        matchesUsername (user.getName ());
        matchesPassword (user.getPassword ());
    }

    private void matchesUsername(@NotNull String username){
        if(! username.matches (USERNAME_REGEX)){
            throw new IllegalArgumentException (String.format ("Не удалось создать пользователя с name %s", username));
        }
    }

    private void matchesPassword(@NotNull String password){
        if(! password.matches (PASSWORD_REGEX)){
            throw new IllegalArgumentException (String.format ("Не удалось создать пользователя с password %s", password));
        }
    }


    public void add(@NotNull AdminDto adminDto){
        if(canEditAdmin ()){
            Set <Roles> roles = new HashSet <> ();
            roles.add (Roles.ADMIN);

            User user = User.builder ()
                    .name (adminDto.getName ())
                    .password (encryptPassword (adminDto.getPassword ()))
                    .blocked (adminDto.isBlocked ())
                    .isAdminManager (adminDto.isAdminManager ())
                    .isAdminScopeManager (adminDto.isAdminScopeManager ())
                    .canCreateAndDeleteScope (true)
                    .maxNumberScope (0L)
                    .maxNumberFolder (0L)
                    .maxStorageSpace (0L)
                    .roles (roles)
                    .build ();

            matchesRegex (user);
            userRepository.save (user);
        }
    }


    public void update(@NotNull AdminDto adminDto){
        Long id = CustomUserDetailsService.getAuthorizedUserId ();
        User currUser = getUserById (id);
        User updatedUser = getUserById (adminDto.getId ());

        if(currUser.getId () != updatedUser.getId () & canEditAdmin ()){
            UserMapper.INSTANCE.fromAdminDtoToUser (adminDto, updatedUser);
            matchesRegex(updatedUser);
            userRepository.save (updatedUser);
        }
    }

    public void deleteAdmin(Long adminId){
        if(canEditAdmin ())
            userRepository.deleteById (adminId);
    }


    public void add(@NotNull AdminScopeDto adminScopeDto){
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
                    .roles (new HashSet <Roles> (){{
                        add (Roles.ADMIN_SCOPES);
                    }})
                    .build ();

            matchesRegex(user);
            userRepository.save (user);
        }
    }

    public void update(@NotNull AdminScopeDto adminScopeDto){
        Long id = CustomUserDetailsService.getAuthorizedUserId ();
        User currUser = getUserById (id);
        User updatedUser = getUserById (adminScopeDto.getId ());

        if(currUser.getId () != updatedUser.getId ()){
            if(canEditAdminScopes ()){
                UserMapper.INSTANCE.fromAdminScopeDtoToUser (adminScopeDto, updatedUser,
                        ScopeMapper.INSTANCE.toScope (scopeRepository, adminScopeDto.getScopePreviewDtos ()));

                matchesRegex(updatedUser);
                userRepository.save (updatedUser);
            }
        }
    }

    public void deleteAdminScope(Long adminScopeId){
        if(canEditAdminScopes ())
            userRepository.deleteById (adminScopeId);
    }

}