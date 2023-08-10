package ru.parma.filesdistr.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.parma.filesdistr.models.Scope;

import java.util.List;

public interface ScopeRepository extends JpaRepository<Scope, Long> {
    List <Scope> findAllByIdIn(List<Long> availableScopesId);
}
