package ru.parma.filesdistr.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.parma.filesdistr.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByName(String name);
}
