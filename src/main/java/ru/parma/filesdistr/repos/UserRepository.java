package ru.parma.filesdistr.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.parma.filesdistr.models.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByName(String name);
    @Query(value = "SELECT * FROM users " +
                        "WHERE (id != ?1 AND (is_admin_scope_manager = TRUE OR is_admin_manager = TRUE))",
            nativeQuery = true)

    List<User> findByIdIsNot(Long id);

    @Query(value = "SELECT * FROM users  " +
            "WHERE (id != ?1 AND is_admin_manager = TRUE)",
            nativeQuery = true)
    List<User> findByIdIsNotAndAdminManagerIsTrue(Long id);

    @Query(value = "SELECT * FROM users  " +
                        "WHERE (id != ?1 AND is_admin_scope_manager = TRUE)",
            nativeQuery = true)
    List<User> findByIdIsNotAndAdminScopeManagerIsTrue(Long id);
}
