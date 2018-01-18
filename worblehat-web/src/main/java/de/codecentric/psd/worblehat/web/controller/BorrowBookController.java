package de.codecentric.psd.worblehat.web.controller;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.codecentric.psd.worblehat.domain.Book;
import de.codecentric.psd.worblehat.domain.BookAlreadyBorrowedException;
import de.codecentric.psd.worblehat.domain.BookService;
import de.codecentric.psd.worblehat.web.formdata.BookBorrowFormData;

/**
 * Controller for BorrowingBook
 */
@RequestMapping("/borrow")
@Controller
public class BorrowBookController {

	private Logger log = Logger.getLogger("BorrowBookControllerLogger");

	private BookService bookService;

	@Autowired
	public BorrowBookController(BookService bookService) {
		this.bookService = bookService;
	}

	@RequestMapping(method = RequestMethod.GET)
	public void setupForm(final ModelMap model) {
		model.put("borrowFormData", new BookBorrowFormData());
	}

	@Transactional
	@RequestMapping(method = RequestMethod.POST)
	public String processSubmit(@ModelAttribute("borrowFormData") @Valid BookBorrowFormData borrowFormData,
								BindingResult result) {
		if (result.hasErrors()) {
			return "borrow";
		}
		Book book = bookService.findBookByIsbn(borrowFormData.getIsbn());
		if (book == null) {
			result.rejectValue("isbn", "notBorrowable");
			return "borrow";
		}
		try {
			bookService.borrowBook(book, borrowFormData.getEmail());
		} catch (BookAlreadyBorrowedException e) {
			log.severe("The book is already borrowed: " + e.getMessage());
			result.rejectValue("isbn", "internalError");
			return "borrow";
		}
		return "home";
	}

	@ExceptionHandler(Exception.class)
	public String handleErrors(Exception ex, HttpServletRequest request) {
		return "home";
	}
}
