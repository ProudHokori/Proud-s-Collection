package proud.collection.repository;


import org.springframework.data.jpa.repository.Query;
import proud.collection.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.UUID;


@Repository
public interface RatingRepository extends JpaRepository<Rating, UUID> {

    // SELECT * FROM Rating WHERE book_id = ‘bookId’
    List<Rating> findAllByBookId(UUID bookId);
    @Query("SELECT r FROM Rating r WHERE r.book.id = :bookId AND r.role = :role")
    List<Rating> findAllByBookIdAndRole(UUID bookId, String role);
}
