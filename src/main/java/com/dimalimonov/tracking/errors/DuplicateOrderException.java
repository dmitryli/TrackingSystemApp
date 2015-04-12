package com.dimalimonov.tracking.errors;

public class DuplicateOrderException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DuplicateOrderException(String packageId) {
		super(ErrorCodes.DUPLICATE_PACKAGE_ERROR_CODE.getErrorCode()
				+ String.format(ErrorMessages.DUPLICATE_PACKAGE_ERROR_MESSAGE.getErrorMessage(), packageId));

	}

	public String getErrorCode() {
		return ErrorCodes.DUPLICATE_PACKAGE_ERROR_CODE.getErrorCode();
	}

}
