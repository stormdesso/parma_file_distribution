package ru.parma.filesdistr.dto;

import lombok.Data;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Data
public class VersionDto {
    Integer id;
    String versionNumber;
    Date dateOfPublication;
    String description;
    boolean showIllustration;
    boolean publish;
    Integer parentId;
    String parentName;
    List<FileDto> illustrations = new ArrayList<>();
    List<FileDto> files = new ArrayList<>();
}
