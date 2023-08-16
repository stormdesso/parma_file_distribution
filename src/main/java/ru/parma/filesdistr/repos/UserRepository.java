package ru.parma.filesdistr.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.parma.filesdistr.enums.Roles;
import ru.parma.filesdistr.models.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByName(String name);
    Optional<User> findByIdAndRolesContaining(Long id, Roles role);
    Long countByName(String name );
    List<User> findByRolesContainingAndIdNot(Roles role, Long id);
}
