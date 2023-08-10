package ru.parma.filesdistr.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.parma.filesdistr.dto.AdminDto;
import ru.parma.filesdistr.dto.AdminScopeDto;
import ru.parma.filesdistr.dto.ScopePreviewDto;
import ru.parma.filesdistr.enums.Roles;
import ru.parma.filesdistr.mappers.UserMapper;
import ru.parma.filesdistr.models.Scope;
import ru.parma.filesdistr.models.User;
import ru.parma.filesdistr.repos.ScopeRepository;
import ru.parma.filesdistr.repos.UserRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    private boolean canEditAdmin () {
        Long id = CustomUserDetailsService.getAuthorizedUserId ();
        User currUser = userRepository.getReferenceById (id);
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
        Long id = CustomUserDetailsService.getAuthorizedUserId ();
        User currUser = userRepository.getReferenceById (id);
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
            users.addAll (userRepository.getAllAdmins (id));
        }

        return UserMapper.INSTANCE.toAdminDtos (users);
    }

    public Set <AdminScopeDto> getAllAdminsScopes () {
        Set <User> users = new HashSet <> ();
        Long id = CustomUserDetailsService.getAuthorizedUserId ();
        if(canEditAdminScopes ()){
            users.addAll (userRepository.getAllAdminsScopes (id));
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


    public void updateAdmin (@NotNull AdminDto adminDto) {
        Long id = CustomUserDetailsService.getAuthorizedUserId ();
        User currUser = userRepository.getReferenceById (id);
        User updatableUser = userRepository.getReferenceById (adminDto.getId ());

        if(currUser.getId () != updatableUser.getId () & canEditAdmin ()){
            applyChangesForAdmin (updatableUser, adminDto);
        }
    }

    private void applyChangesForAdmin (@NotNull User updatableUser, @NotNull AdminDto adminDto) {
        updatableUser.setName (adminDto.getName ());
        updatableUser.setBlocked (adminDto.isBlocked ());
        updatableUser.setAdminManager (adminDto.isAdminManager ());
        updatableUser.setAdminScopeManager (adminDto.isAdminScopeManager ());
        userRepository.save (updatableUser);
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
                    .availableScopes (covertScopePreviewDtosToScopes (adminScopeDto.getScopePreviewDtos ()))
                    .roles (new HashSet <Roles> () {{
                        add (Roles.ADMIN_SCOPES);
                    }})
                    .build ();

            userRepository.save (user);
        }
    }


    public void updateAdminScopes (@NotNull AdminScopeDto adminScopeDto) {
        Long id = CustomUserDetailsService.getAuthorizedUserId ();
        User currUser = userRepository.getReferenceById (id);
        User updatableUser = userRepository.getReferenceById (adminScopeDto.getId ());

        if(currUser.getId () != updatableUser.getId ()){
            if(canEditAdminScopes ()){
                applyChangesForAdminScopes (updatableUser, adminScopeDto);
            }
        }
    }

    private void applyChangesForAdminScopes (@NotNull User updatableUser, @NotNull AdminScopeDto adminScopeDto) {
        updatableUser.setName (adminScopeDto.getName ());
        updatableUser.setBlocked (adminScopeDto.isBlocked ());
        updatableUser.setCanCreateAndDeleteScope (adminScopeDto.isCanCreateAndDeleteScope ());
        updatableUser.setMaxNumberScope (adminScopeDto.getMaxNumberScope ());
        updatableUser.setMaxStorageSpace (adminScopeDto.getMaxStorageSpace ());
        updatableUser.setMaxNumberFolder (adminScopeDto.getMaxNumberFolder ());
        updatableUser.setAvailableScopes (covertScopePreviewDtosToScopes (adminScopeDto.getScopePreviewDtos ()));

        userRepository.save (updatableUser);
    }

    private List <Scope> covertScopePreviewDtosToScopes (@NotNull List <ScopePreviewDto> scopePreviewDtos) {
        List <Long> list = new ArrayList <> ();

        for (ScopePreviewDto scopePreviewDto : scopePreviewDtos) {
            list.add (scopePreviewDto.getId ());
        }

        return scopeRepository.findAllByIdIn (list);
    }


    public void deleteAdminScope (Long adminScopeId) {
        if(canEditAdminScopes ())
            userRepository.deleteById (adminScopeId);
    }
}
