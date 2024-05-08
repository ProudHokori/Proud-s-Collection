package proud.collection.controller;


import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import proud.collection.dto.RatingRequest;
import proud.collection.service.RatingService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import java.util.UUID;
import java.util.logging.Logger;


@Controller
@RequestMapping("/rating")
public class RatingController {

    private static final Logger logger = Logger.getLogger(RatingController.class.getName());

    @Autowired
    private RatingService ratingService;


    @GetMapping("/add/{bookId}")
    public String getReviewForm(@PathVariable UUID bookId,
                                Model model) {

        model.addAttribute("bookId", bookId);
        model.addAttribute("ratingRequest", new RatingRequest());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userRole = auth.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("userRole", userRole);

        return "rating-add";
    }

    @PostMapping("/add")
    public String createReview(@Valid RatingRequest rating,
                               BindingResult result, Model model) {

        
        ratingService.createReview(rating);
        logger.info("Review created successfully");
        return "redirect:/book/" + rating.getBookId();
    }
}

