package ru.parma.filesdistr.mappers;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import ru.parma.filesdistr.dto.FolderDto;
import ru.parma.filesdistr.dto.ViewFolderDto;
import ru.parma.filesdistr.dto.ViewVersionDto;
import ru.parma.filesdistr.models.Folder;
import ru.parma.filesdistr.models.Version;

import java.util.List;

@Mapper
public interface FolderMapper {
    FolderMapper INSTANCE = Mappers.getMapper(FolderMapper.class);
    FolderDto toFolderDto(Folder folder);
    List<FolderDto> toFolderDtos( List<Folder> folders);
    @Named("viewFolder")
    @Mapping(source = "versions", target = "viewVersions", qualifiedByName = "viewVersions")
    ViewFolderDto toViewFolderDto(Folder folder);

    @Named("viewVersions")
    static List<ViewVersionDto> setViewVersions(List<Version> versions) {
        return VersionMapper.INSTANCE.toViewVersionDtos(versions);
    }

    @IterableMapping(qualifiedByName = "viewFolder")
    List<ViewFolderDto> toViewFolderDtos(List<Folder> folders);

    @Named("mapWithoutVersion")
    @Mapping(target = "versions", ignore = true)
    FolderDto mapWithoutVersion(Folder folder);

    @IterableMapping(qualifiedByName="mapWithoutVersion")
    List<FolderDto> toFolderDtosWithoutVersion(List<Folder> folders);

    Folder toFolder(FolderDto folderDto);
}
