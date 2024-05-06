package proud.collection.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;


@ControllerAdvice
public class ErrorController {


    @ExceptionHandler(Throwable.class)
    @ResponseStatus // you can specify specific status code here
    public String exception(final Throwable throwable, final Model model) {
        return "error"; // return error.html
    }
}

