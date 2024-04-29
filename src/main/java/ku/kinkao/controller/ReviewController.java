package ku.kinkao.controller;


import jakarta.validation.Valid;
import ku.kinkao.dto.RatingRequest;
import ku.kinkao.service.BookService;
import ku.kinkao.service.ReviewService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import java.util.UUID;


@Controller
@RequestMapping("/reviews")
public class ReviewController {


    @Autowired
    private ReviewService reviewService;


    @Autowired
    private BookService restaurantService;


    @GetMapping("/show/{restaurantId}")
    public String getReviewPage(@PathVariable UUID restaurantId,
                                Model model) {


        model.addAttribute("restaurant",
                restaurantService.getOneBook(restaurantId));


        return "review-restaurant";
    }


    @GetMapping("/add/{restaurantId}")
    public String getReviewForm(@PathVariable UUID restaurantId,
                                Model model) {


        model.addAttribute("restaurantId", restaurantId);
        model.addAttribute("reviewRequest", new RatingRequest());


        return "review-add";
    }

    @PostMapping("/add")
    public String createReview(@Valid RatingRequest review,
                               BindingResult result, Model model) {


        if (result.hasErrors()) {
            model.addAttribute("restaurantId", review.getBookId());
            return "review-add";
        }


        reviewService.createReview(review);
        return "redirect:/reviews/show/" + review.getBookId();
    }
}

