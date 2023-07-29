package ru.parma.filesdistr.dto;

import lombok.Data;
import ru.parma.filesdistr.models.File;

import java.util.List;
@Data
public class FolderDto {
    private Long id;
    private String name;
    private boolean publish;
    private boolean manifestForIOS;
    private String identifier;
    private File manifestForIOSFile;
    private List<VersionDto> versions;
}
