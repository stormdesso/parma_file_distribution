package ru.parma.filesdistr.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.parma.filesdistr.dto.SavedFileDto;
import ru.parma.filesdistr.models.File;

@Mapper
public interface FileMapper {
    FileMapper INSTANCE = Mappers.getMapper(FileMapper.class);
    SavedFileDto toSaveFileDto(File file);


}
