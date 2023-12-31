package ru.parma.filesdistr.mappers;

import org.jetbrains.annotations.NotNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import ru.parma.filesdistr.dto.AdminDto;
import ru.parma.filesdistr.dto.AdminScopeDto;
import ru.parma.filesdistr.dto.ScopePreviewDto;
import ru.parma.filesdistr.dto.UserCredentialsDto;
import ru.parma.filesdistr.models.Scope;
import ru.parma.filesdistr.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    AdminDto toAdminDto(User user);

    @Mapping(source = "availableScopes", target = "scopePreviewDtos", qualifiedByName = "scopePreviewToScope")
    AdminScopeDto toAdminScopeDto(User user);

    User toUser(UserCredentialsDto userCredentialsDto);

    Set<AdminDto> toAdminDtos (Set<User> users);
    Set<AdminScopeDto> toAdminScopeDtos (Set<User> users);

    @Named("scopePreviewToScope")
    static List<ScopePreviewDto> scopeIdToScope(List<Scope> availableScopes) {

        List<ScopePreviewDto> list = new ArrayList <>();
        for (Scope scope: availableScopes) {
            list.add( ScopePreviewDto.builder()
                            .id(scope.getId())
                            .name(scope.getName())
                    .build());
        }

        return list;
    }

    default void fromAdminDtoToUser (@NotNull AdminDto adminDto, @NotNull User updatedUser){
        updatedUser.setName (adminDto.getName ());
        updatedUser.setBlocked (adminDto.isBlocked ());
        updatedUser.setAdminManager (adminDto.isAdminManager ());
        updatedUser.setAdminScopeManager (adminDto.isAdminScopeManager ());
    }

    default void fromAdminScopeDtoToUser (@NotNull AdminScopeDto adminScopeDto, @NotNull User updatedUser, List <Scope> scopeList){
        updatedUser.setName (adminScopeDto.getName ());
        updatedUser.setBlocked (adminScopeDto.isBlocked ());
        updatedUser.setCanCreateAndDeleteScope (adminScopeDto.isCanCreateAndDeleteScope ());
        updatedUser.setMaxNumberScope (adminScopeDto.getMaxNumberScope ());
        updatedUser.setMaxStorageSpace (adminScopeDto.getMaxStorageSpace ());
        updatedUser.setMaxNumberFolder (adminScopeDto.getMaxNumberFolder ());
        updatedUser.setAvailableScopes (scopeList);
    }

    default void fromUserCredentialsDtoToUser (@NotNull UserCredentialsDto userCredentialsDto, @NotNull User updatedUser, List <Scope> scopeList){
        updatedUser.setName (userCredentialsDto.getName ());
        updatedUser.setPassword(userCredentialsDto.getPassword());
        updatedUser.setAvailableScopes (scopeList);
    }
}
