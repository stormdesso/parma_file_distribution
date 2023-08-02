package ru.parma.filesdistr.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.parma.filesdistr.models.File;

public interface FileRepository extends JpaRepository<File, Long> {


}
