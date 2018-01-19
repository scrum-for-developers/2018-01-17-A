package de.codecentric.psd.worblehat.domain;

import org.joda.time.DateTime;
import org.junit.Test;

import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class BookTest {

    private static final Book TEST_BOOK = new Book("title", "author", "edition", "isbn", 2016,"description");

    @Test
    public void shouldReturnBorrowerEmailOfBorrowedBook() {
        final String expectedEmailAddress = "some@address.com";
        TEST_BOOK.setBorrowing(new Borrowing(TEST_BOOK, expectedEmailAddress, DateTime.now()));
        assertThat("", TEST_BOOK.getBorrowerEmail(), is(expectedEmailAddress));
    }

    @Test
    public void shouldReturnEmptyStringAsBorrowerEmailOfNonBorrowedBook() {
        final String expectedEmailAddress = "some@address.com";
        TEST_BOOK.setBorrowing(null);
        assertThat("", TEST_BOOK.getBorrowerEmail(), is(""));
    }
}