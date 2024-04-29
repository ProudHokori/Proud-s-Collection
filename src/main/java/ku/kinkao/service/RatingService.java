package ku.kinkao.service;


import ku.kinkao.dto.RatingRequest;
import ku.kinkao.entity.Book;
import ku.kinkao.entity.Rating;
import ku.kinkao.repository.BookRepository;
import ku.kinkao.repository.RatingRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.Instant;


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
