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


    @PostMapping("/add")
    public String createReview(@Valid RatingRequest rating,
                               BindingResult result, Model model) {
        try {
            ratingService.createReview(rating);
        }catch (Exception e){
            logger.severe("Error creating rating: " + e.getMessage());
            model.addAttribute("error", e.getMessage());
            return "error";
        }
        logger.info("Rating created successfully");
        return "redirect:/book/" + rating.getBookId();
    }
}

