package ru.parma.filesdistr.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.parma.filesdistr.dto.FileDto;
import ru.parma.filesdistr.dto.TagDto;
import ru.parma.filesdistr.models.File;
import ru.parma.filesdistr.models.Tag;

import java.util.List;

@Mapper
public interface FileMapper {
    FileMapper INSTANCE = Mappers.getMapper(FileMapper.class);
    FileDto toFileDto( File file);
    List<FileDto> toFileDtos( List<File> files);


    Tag toTag( TagDto tagDto );
    List<Tag> toTags( List<TagDto> tagDtos);


    TagDto toTagDto( TagDto tagDto );
    List<TagDto> toTagDtos( List<Tag> tags);
}
