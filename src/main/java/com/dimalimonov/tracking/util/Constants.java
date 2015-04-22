package com.dimalimonov.tracking.util;

public interface Constants {

	public final String SERVER_URI = "http://localhost:8080/tracking";
	public final String REGISTRATION_URI = SERVER_URI + "/registration";
	
	public final String BASE_ACCOUNT_URI = SERVER_URI + "/accounts";
	public final String ACCOUNT_URI = BASE_ACCOUNT_URI + "/%s";
	
	public final String BASE_DELIVERY_URI = ACCOUNT_URI + "/deliveries";
	public final String DELIVERY_URI = BASE_DELIVERY_URI + "/deliveries/%s";
	
	public final String BASE_USER_URI = SERVER_URI + "/users";
	public final String USER_URI = BASE_USER_URI + "/%s";
	
	public final String BASE_FEEDBACK_URI = SERVER_URI + "/feedbacks";
	public final String FEEDBACK_URI = BASE_FEEDBACK_URI + "/%s";
	

}
