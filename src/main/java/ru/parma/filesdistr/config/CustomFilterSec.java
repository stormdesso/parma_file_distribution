package ru.parma.filesdistr.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.Collection;

@RequiredArgsConstructor
public class CustomFilterSec extends GenericFilterBean {

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            WebAuthenticationDetails authenticationDetails = (WebAuthenticationDetails) authentication.getDetails();
        }
        chain.doFilter(request, response);
    }
}