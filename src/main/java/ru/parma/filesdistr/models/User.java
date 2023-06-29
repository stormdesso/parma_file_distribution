package ru.parma.filesdistr.models;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
public class User implements UserDetails {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    @Column(name = "blocked")
    private boolean blocked;

    @Column(name = "isAdminManager")
    private boolean isAdminManager;

    @Column(name = "isAdminScopeManager")
    private boolean isAdminScopeManager;

    @Column(name = "canCreateAndDeleteScope")
    private boolean canCreateAndDeleteScope;

    @Column(name = "maxNumberScope")
    private Long maxNumberScope;

    @Column(name = "maxStorageSpace")
    private Long maxStorageSpace;

    @Column(name = "maxNumberFolder")
    private Long maxNumberFolder;

    @ElementCollection(targetClass = Roles.class, fetch = FetchType.EAGER)    //не создаём отдельную таблицу для Enum-а
    @Column(name = "role_name")
    @CollectionTable(name = "role", joinColumns = @JoinColumn(name = "user_id"))    //join с user по полю user_id
    @Enumerated(EnumType.STRING)// хранится в виде строки
    private Set<Roles> roles = new HashSet<>();


    // security
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public String getUsername() {
        return getName();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !blocked;
    }



    //useless
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return !blocked;
    }
}
