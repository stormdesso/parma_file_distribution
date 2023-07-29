package ru.parma.filesdistr.dto;

import lombok.Data;
import ru.parma.filesdistr.enums.Roles;

import java.util.HashSet;
import java.util.Set;

@Data
public class UserDto {

    private Long id;
    private String name;
    private String password;
    private boolean blocked;
    private boolean isAdminManager;
    private boolean isAdminScopeManager;
    private boolean canCreateAndDeleteScope;
    private Long maxNumberScope;
    private Long maxStorageSpace;
    private Long maxNumberFolder;
    private Set<Roles> roles = new HashSet<>();
}
