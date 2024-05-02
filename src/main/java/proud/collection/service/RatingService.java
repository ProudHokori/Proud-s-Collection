package proud.collection.service;


import proud.collection.dto.RatingRequest;
import proud.collection.entity.Book;
import proud.collection.entity.Rating;
import proud.collection.repository.BookRepository;
import proud.collection.repository.RatingRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.util.List;
import java.util.UUID;


@Service
public class RatingService {


    @Autowired
    private RatingRepository ratingRepository;


    @Autowired
    private BookRepository bookRepository;


    @Autowired
    private ModelMapper modelMapper;


    public void createReview(RatingRequest ratingRequest) {
        Rating rating = modelMapper.map(ratingRequest, Rating.class);
        rating.setCreatedAt(Instant.now());


        Book book =
                bookRepository.findById(ratingRequest.getBookId()).get();

        rating.setBook(book);

        ratingRepository.save(rating);
    }
}
