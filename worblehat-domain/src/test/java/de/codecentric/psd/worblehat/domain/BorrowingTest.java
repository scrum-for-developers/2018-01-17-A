package de.codecentric.psd.worblehat.domain;

import org.joda.time.DateTime;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class BorrowingTest {

    @Test
    public void shouldReturnDueDateForBorrowing(){
        //Given
        Borrowing borrowing = new Borrowing();
        borrowing.setBorrowDate(DateTime.parse("2018-01-01").toDate());

        //When
        Date dueDate = borrowing.getDueDate();

        //Then
        assertThat(dueDate, is(DateTime.parse("2018-01-21").toDate()));
    }
}