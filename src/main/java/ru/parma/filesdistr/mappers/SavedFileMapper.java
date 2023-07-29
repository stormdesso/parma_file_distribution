package ru.parma.filesdistr.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.parma.filesdistr.dto.SavedFileDto;
import ru.parma.filesdistr.models.File;

import java.util.List;

@Mapper
public interface SavedFileMapper {
    SavedFileMapper INSTANCE = Mappers.getMapper(SavedFileMapper.class);
    SavedFileDto toSaveFileDto(File file);
    List<SavedFileDto> toSaveFileDtos( List<File> files);
}
