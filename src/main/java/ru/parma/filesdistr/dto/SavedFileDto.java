package ru.parma.filesdistr.dto;

import lombok.Data;
import ru.parma.filesdistr.models.Tag;

import java.util.List;


@Data
public class SavedFileDto {
    private  Integer id;
    private  String name;
    private  String type;

    private  byte[] data;
    private List<Tag> tags;
}
