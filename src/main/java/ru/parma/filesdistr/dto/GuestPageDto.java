package ru.parma.filesdistr.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GuestPageDto {

    String versionNumber;
    List<FolderDto> folders = new ArrayList<>();
    //TODO !!!не забыть добавить, если необходимо!!!
}
