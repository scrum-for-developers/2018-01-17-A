package de.codecentric.psd.worblehat.web.controller;

import de.codecentric.psd.worblehat.domain.Book;
import de.codecentric.psd.worblehat.domain.BookService;
import de.codecentric.psd.worblehat.web.formdata.ReturnAllBooksFormData;
import de.codecentric.psd.worblehat.web.formdata.ShowAllBooksByBorrowerFormData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/showAllBooksByBorrower")
public class ShowAllBooksByBorrowerController {

    private BookService bookService;

    @Autowired
    public ShowAllBooksByBorrowerController(BookService bookService) {
        this.bookService = bookService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public void prepareView(ModelMap modelMap) {
        modelMap.put("showAllBooksByBorrowerFormData", new ShowAllBooksByBorrowerFormData());
    }

    @RequestMapping(method = RequestMethod.POST)
    public String showAllBooksByBorrower(
            @ModelAttribute("showAllBooksByBorrowerFormData") @Valid
                    ShowAllBooksByBorrowerFormData formData,
            BindingResult result, ModelMap modelMap) {
        if (result.hasErrors()) {
            return "showAllBooksByBorrower";
        } else {
            List<Book> allBooksByBorrower = bookService.getAllBooksByBorrower(formData.getEmailAddress());
            modelMap.put("books", allBooksByBorrower);

            return "bookList";
        }
    }
}
