package ru.parma.filesdistr.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ViewVersionDto {
    private Long id;
    private String versionNumber;
}
