package com.dimalimonov.tracking.errors;

public enum ErrorMessages {
	DUPLICATE_CUSTOMER_ERROR_MESSAGE(": Customer with email %s already exists"), MISSING_DETAILS_ERROR_MESSAGE(
			": Missing field(s) are: %s"), CARRIER_NOT_SUPPORTED_ERROR_MESSAGE("The carrier %s is not supported"), DUPLICATE_PACKAGE_ERROR_MESSAGE(
			": Package with id %s already exists in this account");

	private String errorMessage = null;

	private ErrorMessages(String msg) {
		errorMessage = msg;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
}
