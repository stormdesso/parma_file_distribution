package ru.parma.filesdistr.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GuestPageDto {

    private String versionNumber;
    private List<FolderDto> folders;
    //TODO !!!не забыть добавить, если необходимо!!!
}
