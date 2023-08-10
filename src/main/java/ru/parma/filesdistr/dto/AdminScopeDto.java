package ru.parma.filesdistr.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.util.List;

@Data
@Builder
public class AdminScopeDto {
    private Long id;
    private String name;
    @Nullable
    private String password;
    private boolean blocked;
    private boolean canCreateAndDeleteScope;
    private Long maxNumberScope;
    private Long maxStorageSpace;
    private Long maxNumberFolder;
    private List <ScopePreviewDto> scopePreviewDtos;
}
