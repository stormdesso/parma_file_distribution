package ru.parma.filesdistr.dto;

import lombok.Builder;

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
    "images": [],
    "description": "Это описание пространства МКО",

    "folders":[
        {
            "name": "MKO",
            "description": "Это описание пространства МКО"
        }
    ]
}

 */
@Builder
public class ScopeDto {
    List<FolderDto> folders;
}
