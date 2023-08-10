package ru.parma.filesdistr.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScopePreviewDto {
    private Long id;
    private String name;
}
