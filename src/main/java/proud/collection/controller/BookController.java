package proud.collection.controller;


import jakarta.validation.Valid;
import proud.collection.dto.BookRequest;
import proud.collection.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/book")
public class BookController {


    @Autowired
    private BookService service;


    @GetMapping
    public String getAllBookPage(Model model) {
        model.addAttribute("book", service.getAllBooks());
        return "book-all";
    }


    @GetMapping("/add")
    public String getBookAddPage(Model model) {
        model.addAttribute("bookRequest", new BookRequest());
        return "book-add";
    }


    @PostMapping("/add")
    public String addBook(@Valid BookRequest request,
                          BindingResult result, Model model) {
        if (result.hasErrors())
            return "book-add";


        service.createBook(request);
        return "redirect:/books";
    }
}