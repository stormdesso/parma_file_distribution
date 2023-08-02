package ru.parma.filesdistr.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.parma.filesdistr.dto.FolderDto;
import ru.parma.filesdistr.models.Folder;

import java.util.List;

@Mapper
public interface FolderMapper {
    FolderMapper INSTANCE = Mappers.getMapper(FolderMapper.class);
    FolderDto toFolderDto(Folder folder);
    List<FolderDto> toFolderDtos( List<Folder> folders);

    Folder toFolder(FolderDto folderDto);
}
