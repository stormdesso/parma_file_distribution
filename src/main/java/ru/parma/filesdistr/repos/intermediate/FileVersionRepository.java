package ru.parma.filesdistr.repos.intermediate;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.parma.filesdistr.models.intermediate.FileVersion;

public interface FileVersionRepository extends JpaRepository<FileVersion, Integer> {

}
