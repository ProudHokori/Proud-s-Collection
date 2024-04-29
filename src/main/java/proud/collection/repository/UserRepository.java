package proud.collection.repository;

import proud.collection.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<Users,UUID> {

    // SELECT * FROM User WHERE username = ‘username’
    Users findByUsername(String username);

    // Maybe have one more role thing
}
