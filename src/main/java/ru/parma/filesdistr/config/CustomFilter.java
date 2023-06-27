package ru.parma.filesdistr.config;

import org.springframework.security.access.expression.SecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String currentUserName = authentication.getName();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        SecurityExpressionOperations securityExpressionOperations = (SecurityExpressionOperations) authentication.getPrincipal();
        //boolean hasRoleUser = securityExpressionOperations.hasRole("USER");
        //boolean hasRoleAdmin = securityExpressionOperations.hasRole("ADMIN");

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        System.out.println("Logging Request {} : " + req.getRequestURI());
        chain.doFilter(request, response);
        System.out.println("Logging Response :{} " + res.getContentType());
    }

}
