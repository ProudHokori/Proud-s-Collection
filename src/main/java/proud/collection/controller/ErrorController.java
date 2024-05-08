package proud.collection.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.logging.Level;
import java.util.logging.Logger;


@ControllerAdvice
public class ErrorController {

    private static final Logger logger = Logger.getLogger(ErrorController.class.getName());

    @ExceptionHandler(Throwable.class)
    @ResponseStatus
    public String exception(final Throwable throwable, final Model model) {

        logger.log(Level.SEVERE, "Exception during execution", throwable);

        return "error";
    }
}

