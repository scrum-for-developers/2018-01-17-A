package de.codecentric.psd.worblehat.web.validation;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.routines.ISBNValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ISBNConstraintValidator implements ConstraintValidator<ISBN, String> {

	@Override
	public void initialize(ISBN constraintAnnotation) {
		//Nothing to do here, because we need no initialization
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		// Don't validate null, empty and blank strings, since these are validated by @NotNull, @NotEmpty and @NotBlank
		if(StringUtils.isNotBlank(value)) {
			final ISBNValidator instanceValidator = ISBNValidator.getInstance();
			return instanceValidator.isValidISBN10(value) || instanceValidator.isValidISBN13(value);
		}
		return true;
	}

}
