package ru.parma.filesdistr.dto;

import lombok.Builder;
import lombok.Data;

import java.sql.Date;
import java.util.List;

@Data
@Builder
public class VersionDto {
    private Long id;
    private String versionNumber;
    private Date dateOfPublication;
    private String description;
    private boolean showIllustration;
    private boolean publish;
    private List<FileDto> images;
    private List<FileDto> files;
}
