package com.dimalimonov.tracking.service;

import com.dimalimonov.tracking.domain.Account;

public interface EmailService {

	public abstract void sendWeclomeEmail(Account account);

	public abstract void sendGoodByeEmail(Account account);

	public abstract void sendNotificationEmail(Account account, String orderId, String status);

	public abstract void sendNewOrderEmail(Account account, String orderId, String status);

	public abstract void sendThresholdExceededEmail(Account account, String orderId, String status);

}