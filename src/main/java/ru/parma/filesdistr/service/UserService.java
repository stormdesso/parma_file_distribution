package ru.parma.filesdistr.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.parma.filesdistr.repos.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    UserRepository userRepository;


}
