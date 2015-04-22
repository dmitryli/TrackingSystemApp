package com.dimalimonov.tracking.errors;

public class MissingUserDetailsException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public MissingUserDetailsException(String errorMessage) {

		super(ErrorCodes.MISSING_CUSTOMER_DETAILS_ERROR_CODE.getErrorCode()
				+ String.format(ErrorMessages.MISSING_DETAILS_ERROR_MESSAGE.getErrorMessage(), errorMessage));
	}

}
