package ru.parma.filesdistr.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.parma.filesdistr.models.Scope;
public interface ScopeRepository extends JpaRepository<Scope, Long> {

    @Query(value = "select distinct s from Scope s left join fetch s.folders fo left join fo.versions v where v.id = :versionId and s.id = :scopeId")
    Scope getScopeByScopeIdAndVersionId(@Param("scopeId") Long scopeId ,@Param("versionId") Long versionId);

}
