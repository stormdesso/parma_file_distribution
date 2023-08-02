package ru.parma.filesdistr.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Roles  implements GrantedAuthority {
    USER,
    ADMIN_SCOPES,
    ADMIN,

    ROOT;

    @Override
    public String getAuthority() {
        return name();
    }
}