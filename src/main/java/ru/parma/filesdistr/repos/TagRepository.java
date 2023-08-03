package ru.parma.filesdistr.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.parma.filesdistr.models.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {


}
