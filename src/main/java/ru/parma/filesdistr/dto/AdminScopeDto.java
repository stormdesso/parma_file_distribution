package ru.parma.filesdistr.dto;

import lombok.Builder;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Data
@Builder
public class AdminScopeDto {
    private Long id;
    @NotNull
    private String name;
    private FileDto profilePicture;
    private String password;
    private boolean blocked;
    private boolean canCreateAndDeleteScope;
    @NotNull
    private Long maxNumberScope;
    @NotNull
    private Long maxStorageSpace;
    @NotNull
    private Long maxNumberFolder;
    private List <ScopePreviewDto> scopePreviewDtos;
}
