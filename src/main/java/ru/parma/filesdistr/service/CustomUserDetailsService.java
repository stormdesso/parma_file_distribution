package ru.parma.filesdistr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.parma.filesdistr.models.User;
import ru.parma.filesdistr.repos.UserRepo;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepo userRepo;


    public CustomUserDetailsService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String name) {
        User user = userRepo.findByName(name);
        if (user == null) {
            throw new UsernameNotFoundException(name);
        }



        return user;
    }
}
