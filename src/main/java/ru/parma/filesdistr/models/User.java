package ru.parma.filesdistr.models;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "username")
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

    @Column(name = "is_admin_manager")
    private boolean isAdminManager;

    @Column(name = "is_admin_scope_manager")
    private boolean isAdminScopeManager;

    @Column(name = "can_create_and_delete_scope")
    private boolean canCreateAndDeleteScope;

    @Column(name = "max_number_scope")
    private Long maxNumberScope;

    @Column(name = "max_storage_space")
    private Long maxStorageSpace;

    @Column(name = "max_number_folder")
    private Long maxNumberFolder;

    @Column(name = "role")
    private String role;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)    //не создаём отдельную таблицу для Enum-а
    @Column(name = "role_name")
    @CollectionTable(name = "role", joinColumns = @JoinColumn(name = "user_id"))    //join с user по полю user_id
    @Enumerated(EnumType.STRING)// хранится в виде строки
    private Set<Role> roles = new HashSet<>();


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
        return true;
    }
}
