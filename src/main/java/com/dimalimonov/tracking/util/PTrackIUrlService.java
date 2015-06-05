package com.dimalimonov.tracking.util;

public interface PTrackIUrlService {
	public String getRegistratonURI();

	public String getAccountsURI();

	public String getSingleAccountsURI(String accountId);

	public String getDeliveriesURI(String accountId);

	public String getSingleDeliveryURI(String accountId, String deliveryId);

	public String getUsersURI();

	public String getSingleUsersURI(String userId);

	public String getFeedbackURI();

	public String getSingleFeedbackURI(String feedbackId);
}
