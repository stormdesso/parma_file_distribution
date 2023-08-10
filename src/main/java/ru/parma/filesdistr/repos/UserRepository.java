package ru.parma.filesdistr.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.parma.filesdistr.models.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByName(String name);

    @Query(value = "SELECT * FROM users AS u\n" +
            "         INNER JOIN role AS r\n" +
            "             ON u.id = r.user_id\n" +
            "         WHERE u.id != ?1 AND r.role_name = 'ADMIN'",
            nativeQuery = true)
    List<User> getAllAdmins (Long id);

    @Query(value = "SELECT * FROM users AS u\n" +
            "         INNER JOIN role AS r\n" +
            "             ON u.id = r.user_id\n" +
            "         WHERE u.id != ?1 AND r.role_name = 'ADMIN_SCOPES'",
            nativeQuery = true)
    List<User> getAllAdminsScopes (Long id);
}
