package ru.parma.filesdistr.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.parma.filesdistr.enums.EnumTypePostgreSql;
import ru.parma.filesdistr.enums.Roles;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TypeDef(name = "enum_postgressql", typeClass = EnumTypePostgreSql.class)
public class User implements UserDetails {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_picture_id")//profile_picture_id(fk) user -> id(pk) file
    private File profilePicture;

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

    @ElementCollection(targetClass = Roles.class, fetch = FetchType.EAGER)    //не создаём отдельную таблицу для Enum-а
    @Column(name = "role_name")
    @CollectionTable(name = "role", joinColumns = @JoinColumn(name = "user_id"))    //join с user по полю user_id
    @Enumerated(EnumType.STRING)// хранится в виде строки
    @Type(type = "enum_postgressql")
    private Set<Roles> roles = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_scope",
            joinColumns = {
                    @JoinColumn(name = "user_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "scope_id", referencedColumnName = "id")
            }
    )
    private List<Scope> availableScopes;


    public String getRootPath(){

        if (roles.contains (Roles.ADMIN))
        {
            return Roles.ADMIN.toString () + "//" + getName () + "//";
        }
        else if (roles.contains (Roles.ADMIN_SCOPES))
        {
            return Roles.ADMIN_SCOPES.toString () + "//" + getName () + "//";
        }
        else
            throw new AccessDeniedException("Нет доступа");
    }

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
