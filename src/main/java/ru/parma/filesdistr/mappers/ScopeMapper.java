package ru.parma.filesdistr.mappers;

import org.jetbrains.annotations.NotNull;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.parma.filesdistr.dto.ScopeDto;
import ru.parma.filesdistr.dto.ScopePreviewDto;
import ru.parma.filesdistr.models.Scope;
import ru.parma.filesdistr.repos.ScopeRepository;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface ScopeMapper {
    ScopeMapper INSTANCE = Mappers.getMapper(ScopeMapper.class);
    ScopeDto toScopeDto(Scope scope);

    List<ScopeDto> toScopeDtos(List<Scope> scope);

    default List <Scope> toScope (ScopeRepository scopeRepository, @NotNull List <ScopePreviewDto> scopePreviewDtos){
        List <Long> list = new ArrayList <> ();

        for (ScopePreviewDto scopePreviewDto : scopePreviewDtos) {
            list.add (scopePreviewDto.getId ());
        }

        return scopeRepository.findAllByIdIn (list);
    }
}
