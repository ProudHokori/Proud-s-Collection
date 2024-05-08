package proud.collection.service;


import jakarta.validation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
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
import java.util.Set;
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


    public void createReview(@Valid RatingRequest ratingRequest) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<RatingRequest>> violations = validator.validate(ratingRequest);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        UUID userId = getCurrentUserId();
        UUID bookId = ratingRequest.getBookId();
        float score = ratingRequest.getScore();

        if(score < 0 || score > 5){
            throw new IllegalArgumentException("Score must be between 0 and 5");
        }

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

            if(!user.isEnabled()){
                throw new IllegalStateException("User is not verified yet Please check your email for verification link");
            }
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
        if (principal instanceof DefaultOidcUser){
            DefaultOidcUser user = (DefaultOidcUser) principal;
            Users userEmail = userRepository.findByUsername(user.getName());
            return userEmail.getId();
        }
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
