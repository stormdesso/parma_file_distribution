package ru.parma.filesdistr.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.parma.filesdistr.aop.annotations.LoggableMethod;
import ru.parma.filesdistr.aop.exceptions.EntityIllegalArgumentException;
import ru.parma.filesdistr.aop.exceptions.EntityNotFoundException;
import ru.parma.filesdistr.dto.AdminDto;
import ru.parma.filesdistr.dto.AdminScopeDto;
import ru.parma.filesdistr.dto.UserCredentialsDto;
import ru.parma.filesdistr.enums.Roles;
import ru.parma.filesdistr.mappers.UserMapper;
import ru.parma.filesdistr.models.Scope;
import ru.parma.filesdistr.models.User;
import ru.parma.filesdistr.repos.ScopeRepository;
import ru.parma.filesdistr.repos.UserRepository;
import ru.parma.filesdistr.utils.Utils;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService{

    private final ApplicationContext appContext;
    private final UserRepository userRepository;
    private final ScopeRepository scopeRepository;
    private final AdminPageAccessService adminPageAccessService;
    private final CustomUserDetailsService customUserDetailsService;

    //TODO:обновить regex в соотвествии с описанием приложения
    private static final String USERNAME_REGEX = "^(?=.{8,20}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$";
    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

    private String encryptPassword (String password){
        PasswordEncoder encoder = appContext.getBean (PasswordEncoder.class);
        return encoder.encode (password);
    }

    @LoggableMethod
    private @NotNull User getUserByIdAndRole (Long id, Roles role){
        Optional<User> optUser = userRepository.findByIdAndRolesContaining(id, role);
        if(! optUser.isPresent ()){
            throw new EntityNotFoundException(String.format ("User с id: %d, role: %s  не найден", id, role.toString ()));
        }
        return optUser.get ();
    }

    @LoggableMethod
    public void checkMaxNumberOfScopes (@NotNull AdminScopeDto adminScopeDto){
        if(adminScopeDto.getMaxNumberScope () < adminScopeDto.getScopePreviewDtos ().size ()){
            throw new EntityIllegalArgumentException(String.format ("Превышено допустимое число пространств, доступно" +
                            " %d, выбрано: %d", adminScopeDto.getMaxNumberScope (),
                    adminScopeDto.getScopePreviewDtos ().size ()));
        }
    }

    public void checkMaxNumberOfFolders ( Long maxNumberOfFolder, Long currentNumberOfFolder){
        if(maxNumberOfFolder < currentNumberOfFolder){
            throw new IllegalArgumentException (String.format ("Превышено допустимое число разделов, доступно" +
                            " %d, выбрано: %d", maxNumberOfFolder,
                    currentNumberOfFolder));
        }
    }

    //TODO: проверять, при добавлении нового file от admin_scope
    public void checkMaxDataSize (Long fileSizeInLong){
        if(customUserDetailsService.getAuthorizedUser ().getRoles ().contains (Roles.ADMIN) ||
                customUserDetailsService.getAuthorizedUser ().getRoles ().contains (Roles.ROOT)){
            return;
        }
        double fileSize = Utils.convertFileSizeToGb (fileSizeInLong);

        double maxDataSize = customUserDetailsService.getAuthorizedUser ().getMaxStorageSpace();
        if(maxDataSize < fileSize){
            throw new IllegalArgumentException (String.format ("Превышен допустимый объём публикуемых данных," +
                            " ограничение %s(Гб), загружено: %s(Гб)", maxDataSize,
                    fileSize));
        }
    }

    private void matchesRegex (String username, String password){
        matchesUsername (username);
        matchesPassword (password);
    }

    @LoggableMethod
    private void matchesUsername (@NotNull String username){
        if(! username.matches (USERNAME_REGEX)){
            throw new EntityIllegalArgumentException (String.format ("Не удалось создать пользователя с name %s, т.к" +
                    " name не соответствует требованиям имени", username));
        }
    }

    @LoggableMethod
    private void matchesPassword (@NotNull String password){
        if(! password.matches (PASSWORD_REGEX)){
            throw new EntityIllegalArgumentException (String.format ("Не удалось создать пользователя с password %s, т.к" +
                    " password не соответствует требованиям пароля", password));
        }
    }

    @LoggableMethod
    private void canUseThisName (String name){
        if(userRepository.countByName (name) > 0){
            throw new EntityIllegalArgumentException (String.format ("Пользователь с name %s уже существует", name));
        }
    }

    public Set<AdminDto> getAllAdmins (){
        Long id = CustomUserDetailsService.getAuthorizedUserId ();
        adminPageAccessService.canEditAdmin ();
        Set<User> users = userRepository.findByRolesContainingAndIdNot (Roles.ADMIN, id);

        return UserMapper.INSTANCE.toAdminDtos (users);
    }

    public Set<AdminScopeDto> getAllAdminsScopes (){
        Long id = CustomUserDetailsService.getAuthorizedUserId ();
        adminPageAccessService.canEditAdminScopes ();
        Set<User> users = userRepository.findByRolesContainingAndIdNot (Roles.ADMIN_SCOPES, id);

        return UserMapper.INSTANCE.toAdminScopeDtos (users);
    }

    @Transactional
    public void add (@NotNull AdminDto adminDto){
        adminPageAccessService.canEditAdmin ();
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
        User currUser = customUserDetailsService.getAuthorizedUser();
        User updatedUser = getUserByIdAndRole (adminDto.getId (), Roles.ADMIN);
        String oldName = updatedUser.getName ();

        adminPageAccessService.canEditAdmin ();
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

        adminPageAccessService.canEditAdminScopes ();

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
                .availableScopes (scopeRepository.findScopeByScopePreviewDto (adminScopeDto.getScopePreviewDtos ()))
                .roles (new HashSet<Roles> (){{
                    add (Roles.ADMIN_SCOPES);
                }})
                .build ();

        userRepository.save (user);

    }

    @Transactional
    public void update (@NotNull AdminScopeDto adminScopeDto){
        User currUser = customUserDetailsService.getAuthorizedUser();
        User updatedUser = getUserByIdAndRole (adminScopeDto.getId (), Roles.ADMIN_SCOPES);

        String oldName = updatedUser.getName ();

        if(currUser.getId () != updatedUser.getId ()){
            adminPageAccessService.canEditAdminScopes ();

            matchesRegex (adminScopeDto.getName (), adminScopeDto.getPassword ());

            if(! adminScopeDto.getName ().equals (oldName))//username обновился
            {
                canUseThisName (adminScopeDto.getName ());
            }
            checkMaxNumberOfScopes (adminScopeDto);


            UserMapper.INSTANCE.fromAdminScopeDtoToUser (adminScopeDto, updatedUser,
                    scopeRepository.findScopeByScopePreviewDto (adminScopeDto.getScopePreviewDtos ()));

            userRepository.save (updatedUser);

        }
    }

    @Transactional
    public void add (@NotNull UserCredentialsDto userCredentialsDto, List<Scope> availableScopes) {
        Set<Roles> roles = new HashSet<> ();
        roles.add (Roles.USER);

        matchesRegex (userCredentialsDto.getName (), userCredentialsDto.getPassword ());
        canUseThisName (userCredentialsDto.getName ());

        User user = UserMapper.INSTANCE.toUser(userCredentialsDto);
        user.setAvailableScopes(availableScopes);
        userRepository.save (user);
    }

    @Transactional
    public void update (@NotNull UserCredentialsDto userCredentialsDto, List<Scope> availableScopes){
        User currUser = customUserDetailsService.getAuthorizedUser();
        User updatedUser = getUserByIdAndRole (userCredentialsDto.getId (), Roles.USER);

        String oldName = updatedUser.getName();

        if(currUser.getId () != updatedUser.getId ()){

            matchesRegex (userCredentialsDto.getName (), userCredentialsDto.getPassword ());

            if(! userCredentialsDto.getName ().equals (oldName))//username обновился
            {
                canUseThisName (userCredentialsDto.getName ());
            }

            UserMapper.INSTANCE.fromUserCredentialsDtoToUser(userCredentialsDto, updatedUser, availableScopes);
            userRepository.save (updatedUser);

        }
    }

    @Transactional
    @LoggableMethod
    public void delete (Long userId){
        Optional<User> optUser = userRepository.findById (userId);
        if(! optUser.isPresent ()){
            throw new EntityNotFoundException (String.format ("User с id: %d не найден", userId));
        }
        User user = optUser.get ();
        if(user.getRoles ().containsAll (new HashSet<Roles> (){{
            add (Roles.ROOT);
        }})){
            throw new AccessDeniedException("Нет доступа");
        }
        else if(user.getRoles ().containsAll (new HashSet<Roles> (){{
            add (Roles.ADMIN);
        }})){
            adminPageAccessService.canEditAdmin ();
            userRepository.delete (user);

        }else if(user.getRoles ().containsAll (new HashSet<Roles> (){{
            add (Roles.ADMIN_SCOPES);
        }})){
            adminPageAccessService.canEditAdminScopes ();
            userRepository.delete (user);

        }
    }
}