package ru.parma.filesdistr.mappers;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import ru.parma.filesdistr.dto.*;
import ru.parma.filesdistr.models.Folder;
import ru.parma.filesdistr.models.Scope;

import java.util.List;

@Mapper
public interface ScopeMapper {
    ScopeMapper INSTANCE = Mappers.getMapper(ScopeMapper.class);
    ScopeDto toScopeDto(Scope scope);
    @Named("scopePreview")
    @Mapping(source = "folders", target = "viewFolders", qualifiedByName = "viewFolders")
    ScopePreviewDto toScopePreviewDto(Scope scope);

    @Named("viewFolders")
    static List<ViewFolderDto> setViewFolders(List<Folder> folders) {
        return FolderMapper.INSTANCE.toViewFolderDtos(folders);
    }

    @IterableMapping(qualifiedByName = "scopePreview")
    List<ScopePreviewDto> toScopePreviewDtos(List<Scope> scopes);

    List<ScopeDto> toScopeDtos(List<Scope> scope);
    Scope toScope(ScopeDto scopeDto);
    @Named("scopeWithoutVersions")
    @Mapping(source = "folders", target = "folders", qualifiedByName = "folderWithoutVersion")
    ScopeDto mapWithoutVersion(Scope scope);

    @Named("folderWithoutVersion")
    static List<FolderDto> folderWithoutVersion(List<Folder> folders) {
        return FolderMapper.INSTANCE.toFolderDtosWithoutVersion(folders);
    }

    @IterableMapping(qualifiedByName = "scopeWithoutVersions")
    List<ScopeDto> toScopeDtosWithoutVersion(List<Scope> scope);
}
