package ru.parma.filesdistr.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.lang.Nullable;

@Builder
@Data
public class AdminDto {
    private Long id;
    private String name;
    @Nullable
    private String password;
    private boolean blocked;
    private boolean isAdminManager;
    private boolean isAdminScopeManager;

}
