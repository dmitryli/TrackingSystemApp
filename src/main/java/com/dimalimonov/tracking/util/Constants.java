package com.dimalimonov.tracking.util;

public interface Constants {

	public final String SERVER_URI = "http://localhost:8080/tracking";
	public final String REGISTRATION_URI = SERVER_URI + "/registration";
	
	public final String BASE_ACCOUNT_URI = SERVER_URI + "/accounts";
	public final String ACCOUNT_URI = BASE_ACCOUNT_URI + "/%s";
	
	public final String BASE_ORDER_URI = ACCOUNT_URI + "/orders";
	public final String ORDER_URI = ACCOUNT_URI + "/orders";
	
	public final String BASE_CUSTOMER_URI = SERVER_URI + "/customers";
	public final String CUSTOMER_URI = BASE_CUSTOMER_URI + "/%s";
	
	public final String BASE_FEEDBACK_URI = SERVER_URI + "/feedbacks";
	public final String FEEDBACK_URI = BASE_FEEDBACK_URI + "/%s";
	

}
