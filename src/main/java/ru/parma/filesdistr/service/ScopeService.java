package ru.parma.filesdistr.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import ru.parma.filesdistr.aop.annotations.LoggableMethod;
import ru.parma.filesdistr.aop.exceptions.EntityIllegalArgumentException;
import ru.parma.filesdistr.aop.exceptions.EntityNotFoundException;
import ru.parma.filesdistr.dto.ScopeDto;
import ru.parma.filesdistr.enums.TypeInScopePage;
import ru.parma.filesdistr.mappers.ScopeMapper;
import ru.parma.filesdistr.models.Scope;
import ru.parma.filesdistr.repos.FileSystemRepository;
import ru.parma.filesdistr.repos.ScopeRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScopeService {
    private final ScopeRepository scopeRepository;
    private final FileSystemRepository fileSystemRepository;
    private final FolderService folderService;
    private final VersionService versionService;

    public List<ScopeDto> getAll() {
        List<Scope> scopes = scopeRepository.findAll();
        return ScopeMapper.INSTANCE.toScopeDtosWithoutVersion(scopes);
    }

    public void add(ScopeDto scopeDto) {
        checkDto(scopeDto);
        Scope scope = ScopeMapper.INSTANCE.toScope(scopeDto);
        scopeRepository.save(scope);
    }

    @LoggableMethod
    public void update(ScopeDto scopeDto) {
        checkDto(scopeDto);
        Optional<Scope> existedScope = scopeRepository.findById(scopeDto.getId());
        if (!existedScope.isPresent()) {
            throw new EntityNotFoundException("Такого пространства для обновления не существует");
        }
        Scope scope = ScopeMapper.INSTANCE.toScope(scopeDto);
        scopeRepository.save(scope);
    }

    @LoggableMethod
    private void checkDto(ScopeDto scopeDto) {
        if (scopeDto == null) {
            throw new EntityIllegalArgumentException("Создаваемый объект не может быть null");
        }
        if (scopeDto.getName() == null) {
            throw new EntityIllegalArgumentException("Имя пространства не может быть null");
        }
    }

    @LoggableMethod
    public void delete(long id) {
        Optional<Scope> scope = scopeRepository.findById(id);
        if (!scope.isPresent()) {
            throw new EntityNotFoundException(String.format("Scope с id %d  не найден", id));
        }
        fileSystemRepository.delete(scope.get().getRootPath());
        scopeRepository.delete(scope.get());
    }

    public ScopeDto getDto (Long scopeId) {
        return ScopeMapper.INSTANCE.toScopeDto (get(scopeId));
    }

    @LoggableMethod
    public Scope get(Long scopeId) {
        Optional<Scope> scopeOptional = scopeRepository.findById(scopeId);
        if (!scopeOptional.isPresent()) {
            throw new EntityNotFoundException(String.format("Scope с id %d  не найден", scopeId));
        }
        return scopeOptional.get();
    }
    
    public Scope getScopeBy (@NotNull TypeInScopePage typeInScopePage, @NotNull Long generalId){

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
