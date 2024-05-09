package proud.collection.controller;


import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import proud.collection.dto.RatingRequest;
import proud.collection.service.RatingService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;



@Controller
@RequestMapping("/rating")
public class RatingController {

    Logger logger = LoggerFactory.getLogger(RatingController.class);

    @Autowired
    private RatingService ratingService;


    @PostMapping("/add")
    public String createReview(@Valid RatingRequest rating,
                               BindingResult result, Model model) {
        try {
            ratingService.createReview(rating);
        }catch (Exception e){
            logger.error("Error creating rating: " + e.getMessage());
            model.addAttribute("error", e.getMessage());
            return "error";
        }
        logger.info("Rating created successfully");
        return "redirect:/book/" + rating.getBookId();
    }
}

