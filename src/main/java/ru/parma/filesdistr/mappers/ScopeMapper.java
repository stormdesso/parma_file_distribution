package ru.parma.filesdistr.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.parma.filesdistr.dto.ScopeDto;
import ru.parma.filesdistr.models.Scope;

import java.util.List;

@Mapper
public interface ScopeMapper {
    ScopeMapper INSTANCE = Mappers.getMapper(ScopeMapper.class);
    ScopeDto toScopeDto(Scope scope);

    List<ScopeDto> toScopeDtos(List<Scope> scope);

}
