package com.dimalimonov.tracking.errors;

public class DuplicateCustomerException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DuplicateCustomerException(String email) {
		super(ErrorCodes.DUPLICATE_CUSTOMER_ERROR_CODE.getErrorCode()
				+ String.format(ErrorMessages.DUPLICATE_CUSTOMER_ERROR_MESSAGE.getErrorMessage(), email));

	}

	public String getErrorCode() {
		return ErrorCodes.DUPLICATE_CUSTOMER_ERROR_CODE.getErrorCode();
	}

}
