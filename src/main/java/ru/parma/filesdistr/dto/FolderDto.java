package ru.parma.filesdistr.dto;

import lombok.Data;
import ru.parma.filesdistr.models.Version;

import java.util.ArrayList;
import java.util.List;
@Data
public class FolderDto {
    Integer id;
    Integer parentId;
    String name;
    boolean publish;
    boolean manifestForIOS;
    String identifier;
    byte[] manifestIOS;
    List<VersionDto> versions = new ArrayList<>();
}
