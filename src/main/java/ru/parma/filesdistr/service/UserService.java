package ru.parma.filesdistr.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private final UserRepository userRepository;
    private final ScopeRepository scopeRepository;
    //TODO:обновить regex в соотвествии с описанием приложения
    private static final String USERNAME_REGEX = "^(?=.{8,20}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$";
    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

    private String encryptPassword (String password){
        BCryptPasswordEncoder tmp = new BCryptPasswordEncoder (8);
        return tmp.encode (password);
    }

    private @NotNull User getAuthorizedUser (){

        Long id = CustomUserDetailsService.getAuthorizedUserId ();
        Optional<User> optUser = userRepository.findById (id);
        if(! optUser.isPresent ()){
            throw new EntityNotFoundException (String.format ("User с id %d  не найден", id));
        }

        return optUser.get ();
    }

    private @NotNull User getUserById (Long id){
        Optional<User> optUser = userRepository.findById (id);
        if(! optUser.isPresent ()){
            throw new EntityNotFoundException (String.format ("User с id %d  не найден", id));
        }
        return optUser.get ();
    }

    private void canEditAdmin (){
        User currUser = getAuthorizedUser ();
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

    private void canEditAdminScopes (){
        User currUser = getAuthorizedUser ();
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

    private void checkMaxNumberOfScopes (@NotNull AdminScopeDto adminScopeDto){
        if(adminScopeDto.getMaxNumberScope () < adminScopeDto.getScopePreviewDtos ().size ()){
            throw new IllegalArgumentException (String.format ("Превышено допустимое число пространств, доступно" +
                            " %d, выбрано: %d", adminScopeDto.getMaxNumberScope (),
                    adminScopeDto.getScopePreviewDtos ().size ()));
        }
    }

    private void matchesRegex (String username, String password){
        matchesUsername (username);
        matchesPassword (password);
    }

    private void matchesUsername (@NotNull String username){
        if(! username.matches (USERNAME_REGEX)){
            throw new IllegalArgumentException (String.format ("Не удалось создать пользователя с name %s, т.к" +
                    " name не соответствует требованиям имени", username));
        }
    }

    private void matchesPassword (@NotNull String password){
        if(! password.matches (PASSWORD_REGEX)){
            throw new IllegalArgumentException (String.format ("Не удалось создать пользователя с password %s, т.к" +
                    " password не соответствует требованиям пароля", password));
        }
    }

    private void canUseThisName (String name){
        if(userRepository.countByName (name) > 0){
            throw new IllegalArgumentException (String.format ("Пользователь с name %s уже существует", name));
        }
    }


    public Set<AdminDto> getAllAdmins (){
        Long id = CustomUserDetailsService.getAuthorizedUserId ();
        canEditAdmin ();
        Set<User> users = new HashSet<> (userRepository.findByRolesContainingAndIdNot (Roles.ADMIN, id));

        return UserMapper.INSTANCE.toAdminDtos (users);
    }

    public Set<AdminScopeDto> getAllAdminsScopes (){
        Long id = CustomUserDetailsService.getAuthorizedUserId ();
        canEditAdminScopes ();
        Set<User> users = new HashSet<> (userRepository.findByRolesContainingAndIdNot (Roles.ADMIN_SCOPES, id));

        return UserMapper.INSTANCE.toAdminScopeDtos (users);
    }


    @Transactional
    public void add (@NotNull AdminDto adminDto){
        canEditAdmin ();
        Set<Roles> roles = new HashSet<> ();
        roles.add (Roles.ADMIN);

        matchesRegex (adminDto.getName (), adminDto.getPassword ());
        canUseThisName (adminDto.getName ());

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

        userRepository.save (user);

    }

    @Transactional
    public void update (@NotNull AdminDto adminDto){
        Long id = CustomUserDetailsService.getAuthorizedUserId ();
        User currUser = getUserById (id);
        User updatedUser = getUserById (adminDto.getId ());
        String oldName = updatedUser.getName ();

        if(updatedUser.getRoles ().containsAll (new HashSet<Roles> (){{
            add (Roles.ROOT);
        }})){
            throw new AccessDeniedException ("Нет доступа");
        }
        canEditAdmin ();
        if(currUser.getId () != updatedUser.getId ()){

            matchesRegex (adminDto.getName (), adminDto.getPassword ());

            if(! adminDto.getName ().equals (oldName))//username обновился
            {
                canUseThisName (adminDto.getName ());
            }

            UserMapper.INSTANCE.fromAdminDtoToUser (adminDto, updatedUser);
            userRepository.save (updatedUser);
        }
    }


    @Transactional
    public void add (@NotNull AdminScopeDto adminScopeDto){

        canEditAdminScopes ();

        matchesRegex (adminScopeDto.getName (), adminScopeDto.getPassword ());
        canUseThisName (adminScopeDto.getName ());
        checkMaxNumberOfScopes (adminScopeDto);

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
                .roles (new HashSet<Roles> (){{
                    add (Roles.ADMIN_SCOPES);
                }})
                .build ();

        userRepository.save (user);

    }

    @Transactional
    public void update (@NotNull AdminScopeDto adminScopeDto){

        Long id = CustomUserDetailsService.getAuthorizedUserId ();
        User currUser = getUserById (id);
        User updatedUser = getUserById (adminScopeDto.getId ());
        if(updatedUser.getRoles ().containsAll (new HashSet<Roles> (){{
            add (Roles.ROOT);
        }})){
            throw new AccessDeniedException ("Нет доступа");
        }

        String oldName = updatedUser.getName ();

        if(currUser.getId () != updatedUser.getId ()){
            canEditAdminScopes ();

            matchesRegex (adminScopeDto.getName (), adminScopeDto.getPassword ());

            if(! adminScopeDto.getName ().equals (oldName))//username обновился
            {
                canUseThisName (adminScopeDto.getName ());
            }
            checkMaxNumberOfScopes (adminScopeDto);


            UserMapper.INSTANCE.fromAdminScopeDtoToUser (adminScopeDto, updatedUser,
                    ScopeMapper.INSTANCE.toScope (scopeRepository, adminScopeDto.getScopePreviewDtos ()));

            userRepository.save (updatedUser);

        }
    }



    @Transactional
    public void delete (Long userId){
        User user = getUserById (userId);
        if(user.getRoles ().containsAll (new HashSet<Roles> (){{
            add (Roles.ROOT);
        }})){
            throw new AccessDeniedException ("Нет доступа");
        }
        if(user.getRoles ().containsAll (new HashSet<Roles> (){{
            add (Roles.ADMIN);
        }})){
            canEditAdmin ();
            userRepository.delete (user);

        }else if(user.getRoles ().containsAll (new HashSet<Roles> (){{
            add (Roles.ADMIN_SCOPES);
        }})){
            canEditAdminScopes ();
            userRepository.delete (user);

        }
    }

}