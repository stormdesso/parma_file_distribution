package ru.parma.filesdistr.dto;

import lombok.Builder;
import lombok.Data;
import ru.parma.filesdistr.models.Tag;

import java.util.List;

//для отображения в списке
@Data
@Builder
public class FileDto {
    private  Integer id;
    private  String name;
    private  String type;
    private List<Tag> tags;
}
