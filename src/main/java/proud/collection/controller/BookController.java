package proud.collection.controller;


import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import proud.collection.dto.BookRequest;
import proud.collection.dto.RatingRequest;
import proud.collection.entity.Book;
import proud.collection.entity.Users;
import proud.collection.repository.UserRepository;
import proud.collection.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import proud.collection.service.RatingService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;


@Controller
@RequestMapping("/book")
public class BookController {

    private static final Logger logger = Logger.getLogger(BookController.class.getName());

    @Autowired
    private BookService service;

    @Autowired
    private RatingService ratingService;

    @Autowired
    private UserRepository usersRepository;

    @GetMapping("/{id}")
    public String getBookDetailPage(@PathVariable("id") UUID id, Model model) {
        Book book = service.getOneBook(id);
        System.out.println("book: " + book.getTitleEn());
        float userAverageRating = service.getUserAverageRating(id, "ROLE_USER");
        float adminAverageRating = service.getUserAverageRating(id, "ROLE_ADMIN");
        boolean userHasRated = hasUserRatedBook(id);
        float userRating = getUserRating(id);

        System.out.println("userRating: " + userRating);

        model.addAttribute("book", book);
        model.addAttribute("userAverageRating", userAverageRating);
        model.addAttribute("adminAverageRating", adminAverageRating);
        model.addAttribute("userHasRated", userHasRated);
        model.addAttribute("userRating", userRating);
        model.addAttribute("ratingRequest", new RatingRequest());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userRole = auth.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("userRole", userRole);

        return "book-detail";
    }


    private boolean hasUserRatedBook(UUID bookId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            // Get the current user's ID
            UUID userId = getCurrentUserId();

            // Check if the user has rated the book
            return ratingService.hasUserRatedBook(userId, bookId);
        }
        return false;
    }

    private float getUserRating(UUID bookId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            // Get the current user's ID
            UUID userId = getCurrentUserId();
            // Check if the user has rated the book
            return ratingService.getUserRating(userId, bookId);
        }
        return 0;
    }

    private UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Users user = usersRepository.findByUsername(username);
        return user.getId();
    }

    @GetMapping("/all")
    public String getAllBookPage(Model model) {
        List<Book> books = service.getAllBooks();
        Map<UUID, Float> averageRatings = new HashMap<>();
        for (Book book : books) {
            float averageRating = service.getAverageRating(book.getId());
            averageRatings.put(book.getId(), averageRating);
        }
        model.addAttribute("books", books);
        model.addAttribute("averageRatings", averageRatings);

        return "book-all";
    }



    @GetMapping("/add")
    public String getBookAddPage(Model model) {
        model.addAttribute("bookRequest", new BookRequest());
        return "book-add";
    }


    @PostMapping("/add")
    public String addBook(@Valid BookRequest request,
                          BindingResult result, Model model) throws IOException, SQLException {
        if (result.hasErrors())
            return "book-add";


        service.createBook(request);

        logger.info("Book name: " + request.getTitleTh() + "created successfully");

        return "redirect:/book/all";
    }

    // display image
    @GetMapping("/display")
    public ResponseEntity<byte[]> displayImage(@RequestParam("id") UUID id) throws IOException, SQLException
    {
        Book book = service.getOneBook(id);
        byte [] imageBytes = null;
        imageBytes = book.getImage().getBytes(1,(int) book.getImage().length());
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);
    }
}
