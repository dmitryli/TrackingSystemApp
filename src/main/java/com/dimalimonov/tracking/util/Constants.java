package com.dimalimonov.tracking.util;

public interface Constants {

	public final String SERVER_URI = "http://localhost:8080/tracking";
	public final String REGISTRATION_URI = SERVER_URI + "/registration";
	public final String ACCOUNT_URI = SERVER_URI + "/accounts/%s";
	public final String FEEDBACK_URI = SERVER_URI + "/feedbacks/%s";
	public final String CUSTOMER_URI = SERVER_URI + "/customers/%s";

}
