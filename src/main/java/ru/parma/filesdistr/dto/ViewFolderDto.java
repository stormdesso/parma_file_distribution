package ru.parma.filesdistr.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ViewFolderDto {
    private long id;
    private String name;
    private List<ViewVersionDto> viewVersions;
}
