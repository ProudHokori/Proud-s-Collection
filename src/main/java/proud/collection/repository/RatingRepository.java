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
}
