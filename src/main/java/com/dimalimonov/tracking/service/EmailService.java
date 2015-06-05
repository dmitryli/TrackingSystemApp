package com.dimalimonov.tracking.service;

import com.dimalimonov.tracking.domain.Feedback;

public interface EmailService {

	public abstract void sendWelcomeEmail(String email, String displayName, String account);

	public abstract void sendGoodByeEmail(String email, String displayName, String account);

	public abstract void sendNotificationEmail(String email, String displayName, String deliveryId, String status);

	public abstract void sendNewOrderEmail(String email, String displayName, String deliveryId, String status);

	public abstract void sendThresholdExceededEmail(String email, String displayName, String deliveryId, String status);

	public abstract void sendThresholdExceededEmail(String email, String displayName, String[] deliveryId, String status);

	public abstract void sendFeedbackAddedEmail(Feedback fb);

}