package ru.parma.filesdistr.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.parma.filesdistr.enums.Roles;
import ru.parma.filesdistr.models.User;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByName(String name);
    Optional<User> findByIdAndRolesContaining(Long id, Roles role);
    Long countByName(String name );
    Set<User> findByRolesContainingAndIdNot(Roles role, Long id);
}
