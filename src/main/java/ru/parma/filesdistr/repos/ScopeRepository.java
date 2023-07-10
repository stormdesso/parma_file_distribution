package ru.parma.filesdistr.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.parma.filesdistr.models.Scope;
public interface ScopeRepository extends JpaRepository<Scope, Long> {
    Scope getScopeById(Long id);
}
