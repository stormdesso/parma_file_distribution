package ru.parma.filesdistr.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.parma.filesdistr.models.Folder;
import ru.parma.filesdistr.models.Scope;

public interface FolderRepository extends JpaRepository<Folder, Long> {

}
