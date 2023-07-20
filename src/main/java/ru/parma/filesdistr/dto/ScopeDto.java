package ru.parma.filesdistr.dto;

import lombok.Data;
import ru.parma.filesdistr.models.File;

import java.util.ArrayList;
import java.util.List;



@Data
public class ScopeDto {
    Long id;
    String name;
    String description;
    String copyright;
    boolean showIllustration;
    File icon;
    List<SavedFileDto> images = new ArrayList<>();
    byte[] licenseAgreement;
    List<FolderDto> folders = new ArrayList<>();
}
