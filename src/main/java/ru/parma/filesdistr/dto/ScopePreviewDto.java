package ru.parma.filesdistr.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ScopePreviewDto {
    private Long id;
    private String name;
    private List<ViewFolderDto> viewFolders;
}
