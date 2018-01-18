package de.codecentric.psd.worblehat.web.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.ObjectError;

import de.codecentric.psd.worblehat.domain.BookService;
import de.codecentric.psd.worblehat.web.formdata.ShowAllBooksByBorrowerFormData;

public class ShowAllBooksByBorrowerControllerTest {

    private ShowAllBooksByBorrowerController showAllBooksByBorrowerController;

    private BookService bookService;

    private ShowAllBooksByBorrowerFormData showAllBooksByBorrowerFormData;

    private BindingResult bindingResult;

    @Before
    public void setUp() throws Exception {
        bookService = mock(BookService.class);
        showAllBooksByBorrowerController = new ShowAllBooksByBorrowerController(bookService);
        showAllBooksByBorrowerFormData = new ShowAllBooksByBorrowerFormData();
        bindingResult = new MapBindingResult(new HashMap<>(), "");
    }

    @Test
    public void shouldSetupForm() throws Exception {
        ModelMap modelMap = new ModelMap();

        showAllBooksByBorrowerController.prepareView(modelMap);

        assertThat(modelMap.get("showAllBooksByBorrowerFormData"), is(not(nullValue())));
    }

    @Test
    public void shouldRejectErrors() throws Exception {
        ModelMap modelMap = new ModelMap();
        bindingResult.addError(new ObjectError("", ""));

        String navigateTo = showAllBooksByBorrowerController.showAllBooksByBorrower(showAllBooksByBorrowerFormData, bindingResult, modelMap);

        assertThat(navigateTo, is("showAllBooksByBorrower"));
    }

    @Test
    public void shouldReturnAllBooksAndNavigateHome() throws Exception {
        ModelMap modelMap = new ModelMap();
        String borrower = "someone@codecentric.de";
        showAllBooksByBorrowerFormData.setEmailAddress(borrower);

        String navigateTo = showAllBooksByBorrowerController.showAllBooksByBorrower(showAllBooksByBorrowerFormData, bindingResult, modelMap);

        verify(bookService).getAllBorrowingByBorrower(borrower);
        assertThat(navigateTo, is("borrowedBooksList"));
    }
}
