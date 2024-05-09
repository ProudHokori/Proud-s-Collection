package proud.collection.service;


import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import proud.collection.dto.BookRequest;
import proud.collection.dto.RatingRequest;
import proud.collection.entity.Book;
import proud.collection.entity.Rating;
import proud.collection.repository.BookRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import proud.collection.repository.RatingRepository;


import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;


@Service
public class BookService {

   Logger logger = LoggerFactory.getLogger(BookService.class);


    @Autowired
    private BookRepository repository;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired RatingService ratingService;


    public List<Book> getAllBooks() {
        return repository.findAll();
    }


    public Book getOneBook(UUID id) {
        if (repository.findById(id).isPresent())
            return repository.findById(id).get();
        throw new NoSuchElementException("No book with specified id");
    }


    public void createBook(@Valid BookRequest request) throws IOException, SQLException {
        MultipartFile image = request.getImage();
        byte[] imageBytes = image.getBytes();
        Blob blob = new javax.sql.rowset.serial.SerialBlob(imageBytes);
        Book dao = modelMapper.map(request, Book.class);
        dao.setCreatedAt(Instant.now());
        dao.setImage(blob);
        repository.save(dao);


        RatingRequest ratingRequest = new RatingRequest();

        ratingRequest.setBookId(dao.getId());
        ratingRequest.setScore(request.getRating());
        ratingRequest.setUserRole("ROLE_ADMIN");
        ratingService.createReview(ratingRequest);
        logger.info("Book created successfully with id: " + dao.getId());
    }

    public float getAverageRating(UUID bookId) {
        List<Rating> ratings = ratingRepository.findAllByBookId(bookId);
        if (ratings.isEmpty())
            return 0;
        return (float) ratings.stream().mapToDouble(Rating::getScore).average().getAsDouble();
    }

    public float getUserAverageRating(UUID bookId, String role) {
        List<Rating> ratings = ratingRepository.findAllByBookIdAndRole(bookId, role);
        if (ratings.isEmpty())
            return 0;
        return (float) ratings.stream().mapToDouble(Rating::getScore).average().getAsDouble();
    }
}