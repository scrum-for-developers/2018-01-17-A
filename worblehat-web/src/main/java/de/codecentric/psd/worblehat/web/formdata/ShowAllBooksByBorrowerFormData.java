package de.codecentric.psd.worblehat.web.formdata;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * This class represent the form data of the return book form.
 */
public class ShowAllBooksByBorrowerFormData {

	@NotEmpty(message = "{empty.showAllBooksByBorrowerFormData.emailAddress}")
	@Email(message = "{notvalid.showAllBooksByBorrowerFormData.emailAddress}")
	private String emailAddress;

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

}