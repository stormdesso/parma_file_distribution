package ru.parma.filesdistr.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.parma.filesdistr.models.User;
import ru.parma.filesdistr.repos.UserRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String name) {
        User user = userRepository.findByName(name);
        if (user == null) {
            throw new UsernameNotFoundException(name);
        }
        return user;
    }

    private static Authentication getAuthentication(){
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static boolean tryGetAuthentication(){
        Authentication authentication = getAuthentication();

        return authentication != null && ! (authentication instanceof AnonymousAuthenticationToken);
    }

    public static @Nullable Long getAuthorizedUserId (){
        if(tryGetAuthentication()) {
            return ((User) (getAuthentication().getPrincipal())).getId();
        }
        return null;
    }
}
