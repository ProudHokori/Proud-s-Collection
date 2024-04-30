package proud.collection.service;


import org.springframework.web.multipart.MultipartFile;
import proud.collection.dto.BookRequest;
import proud.collection.entity.Book;
import proud.collection.repository.BookRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.sql.rowset.serial.SerialException;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;


@Service
public class BookService {


    @Autowired
    private BookRepository repository;


    @Autowired
    private ModelMapper modelMapper;


    public List<Book> getAllBooks() {
        return repository.findAll();
    }


    public Book getOneBook(UUID id) {
        if (repository.findById(id).isPresent())
            return repository.findById(id).get();
        throw new NoSuchElementException("No book with specified id");
    }


    public void createBook(BookRequest request) throws IOException, SQLException {
        MultipartFile image = request.getImage();
        byte[] imageBytes = image.getBytes();
        Blob blob = new javax.sql.rowset.serial.SerialBlob(imageBytes);
        Book dao = modelMapper.map(request, Book.class);
        dao.setCreatedAt(Instant.now());
        dao.setImage(blob);
        repository.save(dao);
    }
}
