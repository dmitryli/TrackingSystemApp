package com.dimalimonov.tracking.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.dimalimonov.tracking.domain.Account;
import com.dimalimonov.tracking.service.EmailService;

@Service("emailService")
public class EmailServiceImpl implements EmailService {

	private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

	private final static String FROM = "4tracking101";
	private final static String WELCOME_SUBJECT = "Congratulations on your new account!";
	private final static String DEAR_CUSTOMER = "Dear  %s,\n";
	private final static String WELCOME_TEXT = DEAR_CUSTOMER + FROM
			+ " team welcomes you to the service!\nYour account number is %s";
	private final static String NOTIFICATION_SUBJECT = "Your package %s moved!";
	private final static String NOTIFICATION_TEXT = DEAR_CUSTOMER + "The package %s update is: \n%s";

	private final static String THRESHOLD_SUBJECT = "Your carrier threshold for package %s has exceeded";
	private final static String THRESHOLD_TEXT = DEAR_CUSTOMER
			+ "This is to notify you that the package %s threshold has exceeded\n.Last known status is: \n%s";

	private final static String NEW_ORDER_SUBJECT = "New package %s was added to your account!";
	private final static String NEW_ORDER_TEXT = DEAR_CUSTOMER
			+ "The package %s is now monitored and latest update is: \n%s";

	private final static String GOODBYE_SUBJECT = "We are sorry that you are leaving us!";
	private final static String GOODBYE_TEXT = DEAR_CUSTOMER + FROM
			+ " team is sorry that you deleted you account %s!\nWe hope to see you back soon!";

	@Autowired
	private JavaMailSender mailSender = null;

	@Value("${send.email.on}")
	private boolean isSendEmailOn = true;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dimalimonov.tracking.service.impl.EmailService#sendWeclomeEmail(java
	 * .lang.String)
	 */
	@Override
	public void sendWeclomeEmail(Account account) {
		SimpleMailMessage message = new SimpleMailMessage();

		message.setFrom(FROM);
		// message.setTo(account.getProfile().getEmail());
		message.setSubject(WELCOME_SUBJECT);
		// String welcomeText = String.format(WELCOME_TEXT,
		// account.getProfile().getUserName(), account.getId());
		// message.setText(welcomeText);

		sendEmail(message);
	}

	@Override
	public void sendGoodByeEmail(Account account) {
		SimpleMailMessage message = new SimpleMailMessage();

		message.setFrom(FROM);
		// message.setTo(account.getProfile().getEmail());
		message.setSubject(GOODBYE_SUBJECT);
		// String welcomeText = String.format(GOODBYE_TEXT,
		// account.getProfile().getUserName(), account.getId());
		// message.setText(welcomeText);

		sendEmail(message);

	}

	@Override
	public void sendNewOrderEmail(Account account, String id, String status) {
		SimpleMailMessage message = new SimpleMailMessage();

		message.setFrom(FROM);
		// message.setTo(account.getProfile().getEmail());
		String newOrderSubject = String.format(NEW_ORDER_SUBJECT, id);
		message.setSubject(newOrderSubject);
		// String newOrder = String.format(NEW_ORDER_TEXT,
		// account.getProfile().getUserName(), id, status);
		// message.setText(newOrder);

		sendEmail(message);

	}

	@Override
	public void sendNotificationEmail(Account account, String id, String status) {
		SimpleMailMessage message = new SimpleMailMessage();

		message.setFrom(FROM);
		// message.setTo(account.getProfile().getEmail());
		String notifyOrderSubject = String.format(NOTIFICATION_SUBJECT, id);
		message.setSubject(notifyOrderSubject);

		// String notifyTextOrder = String.format(NOTIFICATION_TEXT,
		// account.getProfile().getUserName(), id, status);
		// message.setText(notifyTextOrder);

		sendEmail(message);
	}

	@Override
	public void sendThresholdExceededEmail(Account account, String id, String status) {
		SimpleMailMessage message = new SimpleMailMessage();

		message.setFrom(FROM);
		// message.setTo(account.getProfile().getEmail());
		String notifyOrderSubject = String.format(THRESHOLD_SUBJECT, id);
		message.setSubject(notifyOrderSubject);

		// String notifyTextOrder = String.format(THRESHOLD_TEXT,
		// account.getProfile().getUserName(), id, status);
		// message.setText(notifyTextOrder);

		sendEmail(message);

	}

	private void sendEmail(SimpleMailMessage mimeMessage) {
		if (isSendEmailOn) {
			mailSender.send(mimeMessage);
		} else {
			logger.info("Email is turned off");
		}

	}

}
