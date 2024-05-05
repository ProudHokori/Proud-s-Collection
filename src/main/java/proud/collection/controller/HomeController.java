package proud.collection.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class HomeController {

    @RequestMapping("/")
    public RedirectView redirectToAllBook() {
        return new RedirectView("/book/all", true);
    }
}
