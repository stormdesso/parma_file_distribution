package ru.parma.filesdistr.dto;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class SavedFileDto {
    private  Long id;
    private  String name;
    private  String type;
    //private  byte[] data;
    private TagDto tag;
}
