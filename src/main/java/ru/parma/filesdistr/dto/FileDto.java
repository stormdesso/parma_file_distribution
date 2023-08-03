package ru.parma.filesdistr.dto;

import lombok.Builder;
import lombok.Data;
import ru.parma.filesdistr.models.Tag;

//для отображения в списке
@Data
@Builder
public class FileDto {
    private  Long id;
    private  String name;
    private  String type;
    private Tag tag;
}
