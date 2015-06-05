package com.dimalimonov.tracking.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PTrackIUrlServiceImpl implements PTrackIUrlService {

	private final String REGISTRATION_URI = "/registration";

	private final String BASE_ACCOUNT_URI = "/accounts";

	private final String BASE_DELIVERY_URI = "/deliveries";

	private final String BASE_USER_URI = "/users";

	private final String BASE_FEEDBACK_URI = "/feedbacks";

	@Value("${app.context}")
	private String contextPath = null;

	public String getRegistratonURI() {
		return contextPath + REGISTRATION_URI;

	}

	public String getAccountsURI() {
		return contextPath + BASE_ACCOUNT_URI;

	}

	public String getSingleAccountsURI(String accountId) {
		return getAccountsURI() + "/" + accountId;

	}

	public String getDeliveriesURI(String accountId) {
		return contextPath + getSingleAccountsURI(accountId) + BASE_DELIVERY_URI;

	}

	public String getSingleDeliveryURI(String accountId, String deliveryId) {
		return getDeliveriesURI(accountId) + "/" + deliveryId;

	}

	public String getUsersURI() {
		return contextPath + BASE_USER_URI;

	}

	public String getSingleUsersURI(String userId) {
		return getUsersURI() + "/" + userId;

	}

	public String getFeedbackURI() {
		return contextPath + BASE_FEEDBACK_URI;

	}

	public String getSingleFeedbackURI(String feedbackId) {
		return getFeedbackURI() + "/" + feedbackId;

	}
}
