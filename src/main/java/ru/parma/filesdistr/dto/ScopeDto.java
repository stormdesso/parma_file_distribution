package ru.parma.filesdistr.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class ScopeDto {
    private Long id;
    private String name;
    private String description;
    private String copyright;
    private boolean showIllustration;

    private List<FolderDto> folders;
    private FileDto icon;
    private List<FileDto> images;
    private FileDto distributionAgreement;
}
