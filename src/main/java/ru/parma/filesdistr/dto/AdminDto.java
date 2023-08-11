package ru.parma.filesdistr.dto;

import lombok.Builder;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Builder
@Data
public class AdminDto {
    private Long id;
    @NotNull
    private String name;
    private FileDto profilePicture;
    private String password;
    private boolean blocked;
    private boolean isAdminManager;
    private boolean isAdminScopeManager;
}
