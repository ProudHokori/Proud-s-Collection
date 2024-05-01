package proud.collection.repository;


import proud.collection.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.UUID;


@Repository
public interface RatingRepository extends JpaRepository<Rating, UUID> {

    // SELECT * FROM Rating WHERE book_id = ‘bookId’
    List<Rating> findAllByBookId(UUID bookId);
    // SELECT * FROM Rating WHERE book_id = ‘bookId’ AND role = ‘role’
//    @Query("SELECT r FROM Rating r JOIN r.book b JOIN b.ratings ra JOIN ra.users u WHERE b.id = :bookId AND u.role = :userRole")
//    List<Rating> findAllByBookIdAndUsersRole(UUID bookId, String userRole);

    List<Rating> findAllByBookIdAndRole(UUID bookId, String role);
}
