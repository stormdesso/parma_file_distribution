package ru.parma.filesdistr.models;

import org.springframework.security.core.GrantedAuthority;

public enum Roles  implements GrantedAuthority {
    USER,
    ADMIN_SCOPES,
    ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}