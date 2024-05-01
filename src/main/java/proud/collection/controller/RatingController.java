package proud.collection.controller;


import jakarta.validation.Valid;
import proud.collection.dto.RatingRequest;
import proud.collection.service.BookService;
import proud.collection.service.RatingService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import java.util.UUID;


@Controller
@RequestMapping("/rating")
public class RatingController {


    @Autowired
    private RatingService ratingService;


    @Autowired
    private BookService bookService;


    @GetMapping("/show/{bookId}")
    public String getReviewPage(@PathVariable UUID bookId,
                                Model model) {


        model.addAttribute("book",
                bookService.getOneBook(bookId));


        return "rating-book";
    }


    @GetMapping("/add/{bookId}")
    public String getReviewForm(@PathVariable UUID bookId,
                                Model model) {


        model.addAttribute("bookId", bookId);
        model.addAttribute("ratingRequest", new RatingRequest());


        return "rating-add";
    }

    @PostMapping("/add")
    public String createReview(@Valid RatingRequest rating,
                               BindingResult result, Model model) {

            System.out.println(result);

        if (result.hasErrors()) {
            model.addAttribute("bookId", rating.getBookId());
            return "rating-add";
        }


        ratingService.createReview(rating);
        return "redirect:/rating/show/" + rating.getBookId();
    }
}

