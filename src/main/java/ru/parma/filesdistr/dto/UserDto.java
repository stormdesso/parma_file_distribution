package ru.parma.filesdistr.dto;

import lombok.Builder;
import lombok.Data;
import ru.parma.filesdistr.enums.Roles;

import java.util.List;
import java.util.Set;

@Data
@Builder
public class UserDto {

    private Long id;
    private String name;
    private boolean blocked;
    private boolean isAdminManager;
    private boolean isAdminScopeManager;
    private boolean canCreateAndDeleteScope;
    private Long maxNumberScope;
    private Long maxStorageSpace;
    private Long maxNumberFolder;
    private Set<Roles> roles;
    private List<ScopeDto> availableScopes;
}
