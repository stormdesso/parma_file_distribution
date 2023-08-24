package ru.parma.filesdistr.dto;

import lombok.Builder;
import lombok.Data;

//для отображения в списке
@Data
@Builder
public class FileDto {
    private  Long id;
    private  String name;
    private  String type;
    private TagDto tag;
    private  String comment;
}
