package ru.parma.filesdistr.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import ru.parma.filesdistr.dto.ScopeDto;
import ru.parma.filesdistr.dto.ScopePreviewDto;
import ru.parma.filesdistr.enums.TypeInScopePage;
import ru.parma.filesdistr.mappers.ScopeMapper;
import ru.parma.filesdistr.models.Scope;
import ru.parma.filesdistr.models.User;
import ru.parma.filesdistr.repos.FileSystemRepository;
import ru.parma.filesdistr.repos.ScopeRepository;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScopeService {
    private final ScopeRepository scopeRepository;
    private final FileSystemRepository fileSystemRepository;
    private final FolderService folderService;
    private final VersionService versionService;
    private final AdminPageAccessService adminPageAccessService;
    private final ScopeAccessService scopeAccessService;
    private final CustomUserDetailsService customUserDetailsService;

    public List<ScopeDto> getAll() {
        List<Scope> scopes = scopeRepository.findAll();
        return ScopeMapper.INSTANCE.toScopeDtosWithoutVersion(scopes);
    }

    public List<ScopePreviewDto> getAvailableScopes() {
        List<Scope> scopes = scopeRepository.findAll();
        List<ScopePreviewDto> scopePreviewDtos = new ArrayList<>();
        try {
            User currentUser = customUserDetailsService.getAuthorizedUser();
            if (adminPageAccessService.isAdmin(currentUser)||
                adminPageAccessService.isRoot(currentUser)) {
                return ScopeMapper.INSTANCE.toScopePreviewDtos(scopes);
            }
            else {
                long userId = CustomUserDetailsService.getAuthorizedUserId();
                for (Scope scope: scopes) {
                    scopeAccessService.tryGetAccessByUserId(TypeInScopePage.SCOPE, scope.getId(), userId);
                    scopePreviewDtos.add(ScopeMapper.INSTANCE.toScopePreviewDto(scope));
                }
            }
        } catch (IOException e) {
            //todo:log:"нет доступа к scope с id %d"
         }
        return scopePreviewDtos;
    }

    public void add(ScopeDto scopeDto) throws AccessDeniedException{
        checkDto(scopeDto);

        scopeAccessService.canCreateAndDeleteScopes(true);

        Scope scope = ScopeMapper.INSTANCE.toScope(scopeDto);
        scopeRepository.save(scope);
    }

    public void update(ScopeDto scopeDto) throws AccessDeniedException{
        checkDto(scopeDto);
        Optional<Scope> existedScope = scopeRepository.findById(scopeDto.getId());
        if (!existedScope.isPresent()) {
            throw new  EntityNotFoundException("Такого пространства для обновления не существует");
        }
        scopeAccessService.tryGetAccessByUserId (TypeInScopePage.SCOPE, scopeDto.getId (),
                CustomUserDetailsService.getAuthorizedUserId());
        Scope scope = ScopeMapper.INSTANCE.toScope(scopeDto);

        scopeRepository.save(scope);
    }

    private void checkDto(ScopeDto scopeDto) {
        if (scopeDto == null) {
            throw new IllegalArgumentException("Создаваемый объект не может быть null");
        }
        if (scopeDto.getName() == null) {
            throw new IllegalArgumentException("Имя пространства не может быть null");
        }
    }

    public void delete(long id) throws AccessDeniedException{
        Optional<Scope> scope = scopeRepository.findById(id);
        if (!scope.isPresent()) {
            throw new EntityNotFoundException(String.format("Scope с id %d  не найден", id));
        }
        scopeAccessService.canCreateAndDeleteScopes(false);
        scopeAccessService.tryGetAccessByUserId (TypeInScopePage.SCOPE, id,
                CustomUserDetailsService.getAuthorizedUserId());

        fileSystemRepository.delete(scope.get().getRootPath());
        scopeRepository.delete(scope.get());
    }

    public ScopeDto getDto (Long scopeId) throws AccessDeniedException{
        scopeAccessService.tryGetAccessByUserId (TypeInScopePage.SCOPE, scopeId,
                CustomUserDetailsService.getAuthorizedUserId ());
        return ScopeMapper.INSTANCE.toScopeDto (get(scopeId));
    }

    public Scope get(Long scopeId) {
        Optional<Scope> scopeOptional = scopeRepository.findById(scopeId);
        if (!scopeOptional.isPresent()) {
            throw new EntityNotFoundException(String.format("Scope с id %d  не найден", scopeId));
        }
        return scopeOptional.get();
    }

    public Scope getScopeBy (@NotNull TypeInScopePage typeInScopePage, @NotNull Long generalId)  {

        Scope scope = null;

        if(typeInScopePage == TypeInScopePage.SCOPE){
            scope = get(generalId);
        }else if(typeInScopePage == TypeInScopePage.FOLDER){
            scope = folderService.get(generalId).getScope ();
        }
        else if(typeInScopePage == TypeInScopePage.VERSION){
            scope = versionService.get(generalId).getFolder ().getScope ();
        }

        return scope;
    }

}
