package proud.collection.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import proud.collection.entity.ConfirmationToken;

import java.util.UUID;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, UUID>{

    ConfirmationToken findByConfirmationToken(String confirmationToken);

}
