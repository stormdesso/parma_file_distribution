package ru.parma.filesdistr.dto;

import lombok.Builder;
import lombok.Data;
import ru.parma.filesdistr.models.File;
import ru.parma.filesdistr.models.Folder;

import java.util.ArrayList;
import java.util.List;

/*
scopes: [
    scope: {
        id:
        folders: [
            folder: { id:, }
           ]
    }
]

{
    "name": "MKO",
    "icon": " ",
    "illustrations": [],
    "description": "Это описание пространства МКО",

    "folders":[
        {
            "name": "MKO",
            "description": "Это описание пространства МКО"
        }
    ]
}

 */
//@Builder
@Data
public class ScopeDto {
    Long id;
    String name;
    String description;
    String copyright;
    boolean showIllustration;
    File icon;
    List<FileDto> images = new ArrayList<>();
    byte[] licenseAgreement;
    List<FolderDto> folders = new ArrayList<>();
}
