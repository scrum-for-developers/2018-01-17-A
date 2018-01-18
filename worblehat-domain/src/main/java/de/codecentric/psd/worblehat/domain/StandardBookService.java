package de.codecentric.psd.worblehat.domain;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * The domain service class for book operations.
 */
@Service
@Transactional
public class StandardBookService implements BookService {
	public StandardBookService() {

	}

	@Autowired
	public StandardBookService(BorrowingRepository borrowingRepository, BookRepository bookRepository) {
		this.borrowingRepository = borrowingRepository;
		this.bookRepository = bookRepository;
	}

	private BorrowingRepository borrowingRepository;

	private BookRepository bookRepository;

	@Override
	public void returnAllBooksByBorrower(String borrowerEmailAddress) {
		List<Borrowing> borrowingsByUser = borrowingRepository
				.findBorrowingsByBorrower(borrowerEmailAddress);
		for (Borrowing borrowing : borrowingsByUser) {
			borrowingRepository.delete(borrowing);
		}
	}

	@Override
	public List<Book> getAllBooksByBorrower(String borrowerEmail) {
		List<Borrowing> borrowingsByUser = borrowingRepository
				.findBorrowingsByBorrower(borrowerEmail);

		List<Book> borrowedBooks = new ArrayList<>();

		for (Borrowing borrowing : borrowingsByUser) {
			borrowedBooks.add(borrowing.getBorrowedBook());
		}
		return borrowedBooks;
	}

	@Override
	public void borrowBook(Book book, String borrowerEmail) throws BookAlreadyBorrowedException {
		Borrowing borrowing = borrowingRepository.findBorrowingForBook(book);
		if (borrowing != null) {
			throw new BookAlreadyBorrowedException("Book is already borrowed");
		} else {
			book = findBookByIsbn(book.getIsbn());
			borrowing = new Borrowing(book, borrowerEmail, new DateTime());
			borrowingRepository.save(borrowing);
		}
	}

	@Override
	public Book findBookByIsbn(String isbn) {

		final List<Book> bookByIsbn = bookRepository.findBookByIsbn(isbn);

		if(!CollectionUtils.isEmpty(bookByIsbn)){
			return bookByIsbn.stream().findFirst().get();
		}
		return null; //null if not found
	}

	@Override
	public List<Book> findAllBooks() {
		return bookRepository.findAllBooks();
	}

	@Override
	public Book createBook(String title, String author, String edition, String isbn, int yearOfPublication,
			String description) {

		if(description != null){
			description = description.replaceAll("(\r\n|\n)", "<br />");
		}

		Book book = new Book(title, author, edition, isbn, yearOfPublication,description);
		return bookRepository.save(book);
	}

	@Override
	public boolean bookExists(String isbn) {
		return findBookByIsbn(isbn) != null;
	}

	@Override
	public void deleteAllBooks() {
		borrowingRepository.deleteAll();
		bookRepository.deleteAll();
	}

}