package com.dimalimonov.tracking.errors;

public enum ErrorCodes {
	DUPLICATE_CUSTOMER_ERROR_CODE("CUST_ERR_0001"), MISSING_CUSTOMER_DETAILS_ERROR_CODE("CUST_ERR_0002"), CARRIER_NOT_SUPPORTED(
			"CARRIER_ERR_0003"), DUPLICATE_PACKAGE_ERROR_CODE("PACK_ERR_0004");

	private String errorCode = null;

	private ErrorCodes(String code) {
		errorCode = code;
	}

	public String getErrorCode() {
		return errorCode;
	}
}
