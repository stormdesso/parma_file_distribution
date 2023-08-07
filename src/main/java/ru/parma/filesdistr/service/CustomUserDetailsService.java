package ru.parma.filesdistr.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.parma.filesdistr.enums.Roles;
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

    public static Long getAuthorizedUserId (){
        return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
    }

    public static boolean isUserHasRole() {
        String role =  SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().findFirst().get().toString();
        return EnumUtils.isValidEnum(Roles.class, role);
    }

}
