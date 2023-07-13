package ru.parma.filesdistr.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.parma.filesdistr.models.LicenseAgreementFileForScope;

public interface LicenseAgreementFileForScopeRepository extends JpaRepository<LicenseAgreementFileForScope, Long> {
    LicenseAgreementFileForScope findByScopeId(int scopeId);
}
