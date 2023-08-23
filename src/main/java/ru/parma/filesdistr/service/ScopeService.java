package ru.parma.filesdistr.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.parma.filesdistr.dto.ScopeDto;
import ru.parma.filesdistr.dto.ScopePreviewDto;
import ru.parma.filesdistr.enums.TypeInScopePage;
import ru.parma.filesdistr.mappers.ScopeMapper;
import ru.parma.filesdistr.models.Scope;
import ru.parma.filesdistr.repos.FileSystemRepository;
import ru.parma.filesdistr.repos.ScopeRepository;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScopeService {
    private final ScopeRepository scopeRepository;
    private final FileSystemRepository fileSystemRepository;

    private final ScopeAccessService scopeAccessService;

    public List<ScopeDto> getAll() {
        List<Scope> scopes = scopeRepository.findAll();
        return ScopeMapper.INSTANCE.toScopeDtosWithoutVersion(scopes);
    }

    public List<ScopePreviewDto> getAvailableScopes() {
        List<Scope> scopes = scopeRepository.findAll();
        List<ScopePreviewDto> scopePreviewDtos = new ArrayList<>();
        try {
            for (Scope scope: scopes) {
                scopeAccessService.tryGetAccess(TypeInScopePage.SCOPE, scope.getId(),
                        CustomUserDetailsService.getAuthorizedUserId());
                scopePreviewDtos.add(ScopeMapper.INSTANCE.toScopePreviewDto(scope));
            }
        } catch (IOException e) {
            //todo:log:"нет доступа к scope с id %d"
         }
        return scopePreviewDtos;
    }

    public void add(ScopeDto scopeDto) {
        checkDto(scopeDto);
        Scope scope = ScopeMapper.INSTANCE.toScope(scopeDto);
        scopeRepository.save(scope);
    }

    public void update(ScopeDto scopeDto) {
        checkDto(scopeDto);
        Optional<Scope> existedScope = scopeRepository.findById(scopeDto.getId());
        if (!existedScope.isPresent()) {
            throw new  EntityNotFoundException("Такого пространства для обновления не существует");
        }
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

    public void delete(long id) {
        Optional<Scope> scope = scopeRepository.findById(id);
        if (!scope.isPresent()) {
            throw new EntityNotFoundException(String.format("Scope с id %d  не найден", id));
        }
        fileSystemRepository.delete(scope.get().getRootPath());
        scopeRepository.delete(scope.get());
    }

    public ScopeDto getScopeById(Long scopeId) {
        Optional<Scope> scopeOptional = scopeRepository.findById(scopeId);
        if (!scopeOptional.isPresent()) {
            throw new EntityNotFoundException(String.format("Scope с id %d  не найден", scopeId));
        }
        return ScopeMapper.INSTANCE.mapWithoutVersion(scopeOptional.get());
    }

}
