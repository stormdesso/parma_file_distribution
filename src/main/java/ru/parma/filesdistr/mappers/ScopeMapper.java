package ru.parma.filesdistr.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.parma.filesdistr.dto.ScopeDto;
import ru.parma.filesdistr.models.Scope;

@Mapper
public interface ScopeMapper {
    ScopeMapper INSTANCE = Mappers.getMapper(ScopeMapper.class);
    ScopeDto toScopeDto(Scope scope);
}
