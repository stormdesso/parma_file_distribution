package ru.parma.filesdistr.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.parma.filesdistr.enums.Roles;
import ru.parma.filesdistr.models.User;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByName(String name);
    Optional<User> findByIdAndRolesContaining(Long id, Roles role);
    Long countByName(String name );
    Set<User> findByRolesContainingAndIdNot(Roles role, Long id);
    //TODO:getUserById без root

    Optional<User> findByIdAndRolesNotContains(Long id, Roles role);

    default User findUserWithoutRoot(Long id){
        Optional<User> optUser = findByIdAndRolesNotContains(id, Roles.ROOT);
        if(! optUser.isPresent ()){
            throw new EntityNotFoundException (String.format ("User с id: %d не найден", id));
        }

        return optUser.get ();
    }

}
