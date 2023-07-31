package ru.parma.filesdistr.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.parma.filesdistr.models.Scope;
public interface ScopeRepository extends JpaRepository<Scope, Long> {

/*    @Query(value = "select distinct s from Scope s left join fetch s.folders fo left join fo.versions v where v.id = :versionId")
    Scope getScopeByVersionId(@Param("versionId") Long versionId);

    @Query(value = "select distinct s from  Scope s left join fetch s.folders fo left join fo.versions v where v.id = (select max(v.id) from v) and s.id = :scopeId")
    Scope getScopeByScopeIdAndLatestVersion(@Param("scopeId") Long scopeId);*/
}
