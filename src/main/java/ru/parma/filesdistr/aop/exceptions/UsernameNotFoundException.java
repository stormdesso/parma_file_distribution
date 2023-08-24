package ru.parma.filesdistr.aop.exceptions;

import org.springframework.util.Assert;

public class UsernameNotFoundException extends BaseException{
    public UsernameNotFoundException(String username) {
        super(formatMessage(username));
    }

    private static String formatMessage(String username) {
        Assert.hasText(username, "Username не может быть пустым");
        return String.format("Username %s not found", username);
    }
}
