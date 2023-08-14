package ru.parma.filesdistr.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.parma.filesdistr.enums.Roles;
import ru.parma.filesdistr.models.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByName(String name);
    List<User> findByRolesContainingAndIdNot(Roles role, Long id);
}
