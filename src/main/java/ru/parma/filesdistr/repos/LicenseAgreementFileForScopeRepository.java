package ru.parma.filesdistr.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.parma.filesdistr.models.LicenseAgreementFileForScope;

public interface LicenseAgreementFileForScopeRepository extends JpaRepository<LicenseAgreementFileForScope, Long> {
    LicenseAgreementFileForScope findByScopeId(int scopeId);
}
