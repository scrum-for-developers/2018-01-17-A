package de.codecentric.psd.worblehat.web.controller;

import de.codecentric.psd.worblehat.domain.BookService;
import de.codecentric.psd.worblehat.domain.Borrowing;
import de.codecentric.psd.worblehat.web.formdata.ShowAllBooksByBorrowerFormData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
			List<Borrowing> allBorrowingByBorrower = bookService.getAllBorrowingByBorrower(formData.getEmailAddress());
			modelMap.put("borrowings", allBorrowingByBorrower);
			return "borrowedBooksList";
		}
	}
}
