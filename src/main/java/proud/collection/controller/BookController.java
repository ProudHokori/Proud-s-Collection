package proud.collection.controller;


import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import proud.collection.dto.BookRequest;
import proud.collection.entity.Book;
import proud.collection.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Controller
@RequestMapping("/book")
public class BookController {


    @Autowired
    private BookService service;

    @GetMapping("/{id}")
    public String getBookDetailPage(@PathVariable("id") UUID id, Model model) {
        Book book = service.getOneBook(id);
        System.out.println("book: " + book.getTitleEn());
        float userAverageRating = service.getUserAverageRating(id, "ROLE_USER");
        float adminAverageRating = service.getUserAverageRating(id, "ROLE_ADMIN");

        model.addAttribute("book", book);
        model.addAttribute("userAverageRating", userAverageRating);
        model.addAttribute("adminAverageRating", adminAverageRating);
        return "book-detail";
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
