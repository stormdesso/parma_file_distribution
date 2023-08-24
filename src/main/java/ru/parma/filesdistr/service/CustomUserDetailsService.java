package ru.parma.filesdistr.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.parma.filesdistr.aop.annotations.LoggableMethod;
import ru.parma.filesdistr.aop.exceptions.AccessDeniedException;
import ru.parma.filesdistr.aop.exceptions.EntityNotFoundException;
import ru.parma.filesdistr.aop.exceptions.UsernameNotFoundException;
import ru.parma.filesdistr.models.User;
import ru.parma.filesdistr.repos.UserRepository;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @LoggableMethod
    public UserDetails loadUserByUsername(String name) {
        User user = userRepository.findByName(name);
        if (user == null) {
            throw new UsernameNotFoundException(name);
        }
        return user;
    }

    public static Authentication getAuthentication(){
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static boolean isAuthenticated(){
        Authentication authentication = getAuthentication();
        return authentication != null && ! (authentication instanceof AnonymousAuthenticationToken);
    }

    public static @Nullable Long getAuthorizedUserId (){
        if(isAuthenticated()) {
            return ((User) (getAuthentication().getPrincipal())).getId();
        }
        return null;
    }

    @LoggableMethod
    public @NotNull User getAuthorizedUser (){

        Long id = CustomUserDetailsService.getAuthorizedUserId ();
        if(id == null){
            throw new AccessDeniedException("Пользователь не авторизован");
        }
        Optional<User> optUser = userRepository.findById (id);
        if(! optUser.isPresent ()){
            throw new EntityNotFoundException(String.format ("User с id %d  не найден", id));
        }

        return optUser.get ();
    }
}
