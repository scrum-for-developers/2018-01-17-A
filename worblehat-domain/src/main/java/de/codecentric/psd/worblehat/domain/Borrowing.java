package de.codecentric.psd.worblehat.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.joda.time.DateTime;

/**
 * Borrowing Entity
 */
@Entity
public class Borrowing implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; // NOSONAR

    private String borrowerEmailAddress;

    @Temporal(TemporalType.DATE)
    private Date borrowDate;

    private Borrowing() {
        // for JPA
    }

    @OneToOne()
    private Book borrowedBook;

    public String getBorrowerEmailAddress() {
        return borrowerEmailAddress;
    }

    /**
     * @param book                 The borrowed book
     * @param borrowerEmailAddress The borrowers e-mail Address
     * @param borrowDate           The borrow date
     */
    public Borrowing(Book book, String borrowerEmailAddress, DateTime borrowDate) {
        super();
        this.borrowedBook = book;
        this.borrowerEmailAddress = borrowerEmailAddress;
        this.borrowDate = borrowDate.toDate();
    }

}
