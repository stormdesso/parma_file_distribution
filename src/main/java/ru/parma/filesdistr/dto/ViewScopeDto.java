package ru.parma.filesdistr.dto;

import lombok.Data;
import ru.parma.filesdistr.models.File;

import java.util.ArrayList;
import java.util.List;

@Data
public class ViewScopeDto {

    String versionNumber;
    List<FolderDto> folders = new ArrayList<>();
    //TODO !!!не забыть добавить, если необходимо!!!
}
