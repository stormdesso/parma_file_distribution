package ru.parma.filesdistr.config;

import org.springframework.security.access.expression.SecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebSecurityExpressionRoot;

public class CustomWebSecurityExpressionHandler extends DefaultWebSecurityExpressionHandler {
    @Override
    protected SecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication, FilterInvocation fi) {
        WebSecurityExpressionRoot root = new WebSecurityExpressionRoot(authentication, fi);
        root.setDefaultRolePrefix(""); // Remove the "ROLE_" prefix from roles
        return root;
    }
}
