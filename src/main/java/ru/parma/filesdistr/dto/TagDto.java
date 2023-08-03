package ru.parma.filesdistr.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TagDto {
    private Long id;
    private String letter;
    private String backgroundColor;
    private String letterColor;
}
