package de.codecentric.psd.worblehat.domain;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class StandardBookServiceTest {

	private BorrowingRepository borrowingRepository;

	private BookRepository bookRepository;

	private BookService bookService;

	private static final String BORROWER_EMAIL = "someone@codecentric.de";

	private static final DateTime NOW = DateTime.now();

	private static final Book TEST_BOOK = new Book("title", "author", "edition", "isbn", 2016,"description");

	private static final Book NEWER_TEST_BOOK = new Book("New Title", "new author", "new edition", "new isbn", 2017,"");

	@Before
	public void setup() throws Exception {
		borrowingRepository = mock(BorrowingRepository.class);
		bookRepository = mock(BookRepository.class);
		bookService = new StandardBookService(borrowingRepository, bookRepository);
	}

	@Test
	public void shouldReturnAllBooksOfOnePerson() {
		Borrowing borrowing = new Borrowing(TEST_BOOK, BORROWER_EMAIL, NOW);
		List<Borrowing> result = Collections.singletonList(borrowing);
		when(borrowingRepository.findBorrowingsByBorrower(BORROWER_EMAIL))
		.thenReturn(result);
		bookService.returnAllBooksByBorrower(BORROWER_EMAIL);
		verify(borrowingRepository).delete(borrowing);
	}

	@Test
	public void shouldGetAllBooksOfOnePerson() {
		Borrowing borrowing = new Borrowing(TEST_BOOK, BORROWER_EMAIL, NOW.minusDays(10));
		Borrowing borrowing1 = new Borrowing(NEWER_TEST_BOOK, BORROWER_EMAIL, NOW);

		List<Borrowing> result = new ArrayList<>();
		result.add(borrowing);
		result.add(borrowing1);

		when(borrowingRepository.findBorrowingsByBorrower(BORROWER_EMAIL))
				.thenReturn(result);

        List<Book> books = bookService.getAllBooksByBorrower(BORROWER_EMAIL);

        assertThat(books.size(), is(2));

        assertThat(books.get(0).getAuthor(), is(TEST_BOOK.getAuthor()));
        assertThat(books.get(0).getBorrowerEmail(), is(TEST_BOOK.getBorrowerEmail()));
        assertThat(books.get(0).getEdition(), is(TEST_BOOK.getEdition()));
        assertThat(books.get(0).getIsbn(), is(TEST_BOOK.getIsbn()));
        assertThat(books.get(0).getTitle(), is(TEST_BOOK.getTitle()));
        assertThat(books.get(0).getYearOfPublication(), is(TEST_BOOK.getYearOfPublication()));
        assertThat(books.get(0).getDescription(), is(TEST_BOOK.getDescription()));

        assertThat(books.get(1).getAuthor(), is(NEWER_TEST_BOOK.getAuthor()));
        assertThat(books.get(1).getBorrowerEmail(), is(NEWER_TEST_BOOK.getBorrowerEmail()));
        assertThat(books.get(1).getEdition(), is(NEWER_TEST_BOOK.getEdition()));
        assertThat(books.get(1).getIsbn(), is(NEWER_TEST_BOOK.getIsbn()));
        assertThat(books.get(1).getTitle(), is(NEWER_TEST_BOOK.getTitle()));
        assertThat(books.get(1).getYearOfPublication(), is(NEWER_TEST_BOOK.getYearOfPublication()));
	}

	@Test
	public void shouldSaveBorrowingWithBorrowerEmail() throws Exception {
		when(borrowingRepository.findBorrowingForBook(TEST_BOOK)).thenReturn(null);
		ArgumentCaptor<Borrowing> borrowingArgumentCaptor = ArgumentCaptor.forClass(Borrowing.class);
		bookService.borrowBook(TEST_BOOK, BORROWER_EMAIL);
		verify(borrowingRepository).save(borrowingArgumentCaptor.capture());
		assertThat(borrowingArgumentCaptor.getValue().getBorrowerEmailAddress(), is(BORROWER_EMAIL));
	}

	@Test(expected = BookAlreadyBorrowedException.class)
	public void shouldThrowExceptionWhenBookAlreadyBorrowed() throws Exception {
		when(borrowingRepository.findBorrowingForBook(TEST_BOOK)).thenReturn(new Borrowing(TEST_BOOK, BORROWER_EMAIL, NOW));
		bookService.borrowBook(TEST_BOOK, BORROWER_EMAIL);
	}

	@Test
	public void shouldCreateBook() throws Exception {
		ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);
		bookService.createBook(TEST_BOOK.getTitle(), TEST_BOOK.getAuthor(), TEST_BOOK.getEdition(),
				TEST_BOOK.getIsbn(), TEST_BOOK.getYearOfPublication(),TEST_BOOK.getDescription());
		verify(bookRepository).save(bookArgumentCaptor.capture());
		assertThat(bookArgumentCaptor.getValue().getTitle(), is(TEST_BOOK.getTitle()));
		assertThat(bookArgumentCaptor.getValue().getAuthor(), is(TEST_BOOK.getAuthor()));
		assertThat(bookArgumentCaptor.getValue().getEdition(), is(TEST_BOOK.getEdition()));
		assertThat(bookArgumentCaptor.getValue().getIsbn(), is(TEST_BOOK.getIsbn()));
		assertThat(bookArgumentCaptor.getValue().getYearOfPublication(), is(TEST_BOOK.getYearOfPublication()));
		assertThat(bookArgumentCaptor.getValue().getDescription(), is(TEST_BOOK.getDescription()));
	}

	@Test
	public void shouldFindAllBooks() throws Exception {
		List<Book> expectedBooks = new ArrayList<>();
		expectedBooks.add(TEST_BOOK);
		when(bookRepository.findAllBooks()).thenReturn(expectedBooks);
		List<Book> actualBooks = bookService.findAllBooks();
		assertThat(actualBooks, is(expectedBooks));
	}

	@Test
	public void shouldFindexistingBooks() throws Exception {
		when(bookRepository.findBookByIsbn(TEST_BOOK.getIsbn())).thenReturn(TEST_BOOK);
		Book actualBook = bookRepository.findBookByIsbn(TEST_BOOK.getIsbn());
		assertThat(actualBook, is(TEST_BOOK));
	}

	@Test
	public void shouldVerifyExistingBooks() throws Exception {
		when(bookRepository.findBookByIsbn(TEST_BOOK.getIsbn())).thenReturn(TEST_BOOK);
		Boolean bookExists = bookService.bookExists(TEST_BOOK.getIsbn());
		assertTrue(bookExists);
	}

	@Test
	public void shouldDeleteAllBooksAndBorrowings() throws Exception {
		bookService.deleteAllBooks();
		verify(bookRepository).deleteAll();
		verify(borrowingRepository).deleteAll();
	}
}
