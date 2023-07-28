package ru.parma.filesdistr.dto;

import lombok.Data;


@Data
public class SavedFileDto {
    private  Integer id;
    private  String name;
    private  String type;

    private  byte[] data;
    //TODO:добавить тег
    //private final List<Tag> tags = new ArrayList<>();
}
