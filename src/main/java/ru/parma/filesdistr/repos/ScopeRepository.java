package ru.parma.filesdistr.repos;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.parma.filesdistr.dto.ScopePreviewDto;
import ru.parma.filesdistr.models.Scope;

import java.util.ArrayList;
import java.util.List;

public interface ScopeRepository extends JpaRepository<Scope, Long> {
    List <Scope> findAllByIdIn(List<Long> availableScopesId);

    default List <Scope> findScopeByScopePreviewDto (@NotNull List <ScopePreviewDto> scopePreviewDtos){
        List <Long> list = new ArrayList<> ();

        for (ScopePreviewDto scopePreviewDto : scopePreviewDtos) {
            list.add (scopePreviewDto.getId ());
        }

        return findAllByIdIn (list);
    }
}
