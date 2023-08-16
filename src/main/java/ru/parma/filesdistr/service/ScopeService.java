package ru.parma.filesdistr.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.parma.filesdistr.dto.ScopeDto;
import ru.parma.filesdistr.mappers.ScopeMapper;
import ru.parma.filesdistr.models.Scope;
import ru.parma.filesdistr.repos.FileSystemRepository;
import ru.parma.filesdistr.repos.ScopeRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScopeService {
    private final ScopeRepository scopeRepository;
    private final FileSystemRepository fileSystemRepository;

    public List<ScopeDto> getAll() {
        List<Scope> scopes = scopeRepository.findAll();
        return ScopeMapper.INSTANCE.toScopeDtosWithoutVersion(scopes);
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
    }

    public ScopeDto getScopeById(Long scopeId) {
        Optional<Scope> scopeOptional = scopeRepository.findById(scopeId);
        if (!scopeOptional.isPresent()) {
            throw new EntityNotFoundException(String.format("Scope с id %d  не найден", scopeId));
        }
        return ScopeMapper.INSTANCE.mapWithoutVersion(scopeOptional.get());
    }
}
