package ru.parma.filesdistr.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.parma.filesdistr.dto.FileDto;
import ru.parma.filesdistr.models.File;

@Mapper
public interface FileMapper {
    FileDto toDto(File file);
    FileMapper INSTANCE = Mappers.getMapper(FileMapper.class);
}
