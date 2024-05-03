package proud.collection.service;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import proud.collection.dto.RatingRequest;
import proud.collection.entity.Book;
import proud.collection.entity.Rating;
import proud.collection.entity.Users;
import proud.collection.repository.BookRepository;
import proud.collection.repository.RatingRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import proud.collection.repository.UserRepository;


import java.time.Instant;
import java.util.Optional;
import java.util.UUID;


@Service
public class RatingService {


    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;


    @Autowired
    private ModelMapper modelMapper;


    public void createReview(RatingRequest ratingRequest) {

        UUID userId = getCurrentUserId(); // Implement a method to get the current user's ID
        UUID bookId = ratingRequest.getBookId();
        float score = ratingRequest.getScore();

        Optional<Rating> existingRatingOptional = ratingRepository.findByUserIdAndBookId(userId, bookId);

        if(existingRatingOptional.isPresent()){
            Rating existingRating = existingRatingOptional.get();
            existingRating.setScore(score);
            existingRating.setCreatedAt(Instant.now());
            ratingRepository.save(existingRating);
        }else{


            Rating newRating = new Rating();
            newRating.setScore(score);
            newRating.setCreatedAt(Instant.now());

            Book book = bookRepository.findById(bookId).orElseThrow(() -> new IllegalArgumentException("Book not found"));
            newRating.setBook(book);

            Users user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
            newRating.setUser(user);

            newRating.setRole(ratingRequest.getUserRole());

            ratingRepository.save(newRating);
    }
    }

    private UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User not authenticated");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            // Assuming you store user's UUID as username in UserDetails
            String username = userDetails.getUsername();
            // Fetch the user by username and return the ID
            Users user = userRepository.findByUsername(username);
            return user.getId();
        } else {
            throw new IllegalStateException("Invalid principal type");
        }
    }

    public boolean hasUserRatedBook(UUID userId, UUID bookId) {
        return ratingRepository.findByUserIdAndBookId(userId, bookId).isPresent();
    }

    public float getUserRating(UUID userId, UUID bookId) {
        return ratingRepository.findByUserIdAndBookId(userId, bookId)
                .map(Rating::getScore)
                .orElse(0f);
    }

}
