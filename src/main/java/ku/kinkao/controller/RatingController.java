package ku.kinkao.controller;


import jakarta.validation.Valid;
import ku.kinkao.dto.RatingRequest;
import ku.kinkao.service.BookService;
import ku.kinkao.service.RatingService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import java.util.UUID;


@Controller
@RequestMapping("/reviews")
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


        return "review-book";
    }


    @GetMapping("/add/{bookId}")
    public String getReviewForm(@PathVariable UUID bookId,
                                Model model) {


        model.addAttribute("bookId", bookId);
        model.addAttribute("ratingRequest", new RatingRequest());


        return "rating-add";
    }

    @PostMapping("/add")
    public String createReview(@Valid RatingRequest review,
                               BindingResult result, Model model) {


        if (result.hasErrors()) {
            model.addAttribute("restaurantId", review.getBookId());
            return "rating-add";
        }


        ratingService.createReview(review);
        return "redirect:/reviews/show/" + review.getBookId();
    }
}

