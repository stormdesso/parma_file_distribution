package ru.parma.filesdistr.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.parma.filesdistr.dto.VersionDto;
import ru.parma.filesdistr.models.Version;

import java.util.List;

@Mapper
public interface VersionMapper {
    VersionMapper INSTANCE = Mappers.getMapper(VersionMapper.class);
    VersionDto toVersionDto(Version version);
    List<VersionDto> toVersionDtos( List<Version> scope);

    Version toVersion(VersionDto versionDto);
}
