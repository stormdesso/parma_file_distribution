package ru.parma.filesdistr.dto;

import lombok.Data;

import java.util.Date;

@Data
public class FileDto {
    Integer id;
    String name;
    Double size;
    String type;
    Date dateCreated;
    String location;
    byte[] data;
    //@Nullable
    //List<Tag> tags;
}
