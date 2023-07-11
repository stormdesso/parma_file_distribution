package ru.parma.filesdistr.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.parma.filesdistr.models.Folder;
import ru.parma.filesdistr.models.Version;

public interface VersionRepository extends JpaRepository<Version, Long> {

}
