package com.dimalimonov.tracking.service.impl;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.dimalimonov.tracking.domain.Feedback;
import com.dimalimonov.tracking.service.EmailService;

@Service("emailService")
public class EmailServiceImpl implements EmailService {

	private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

	@Autowired
	private JavaMailSender mailSender = null;

	@Value("${send.email.on}")
	private boolean isSendEmailOn = true;

	@Autowired
	private MessageSource emailMessages = null;

	private Locale locale = Locale.US;

	@Override
	public void sendWelcomeEmail(String email, String displayName, String account) {
		String subject = emailMessages.getMessage("welcome.email.subject", null, Locale.US);
		String text = emailMessages.getMessage("welcome.email.text", new Object[] { displayName, account }, locale);
		sendEmail(email, subject, text);

	}

	@Override
	public void sendGoodByeEmail(String email, String displayName, String account) {
		String subject = emailMessages.getMessage("leaving.email.subject", null, Locale.US);
		String text = emailMessages.getMessage("leaving.email.text", new Object[] { displayName, account }, locale);
		sendEmail(email, subject, text);

	}

	@Override
	public void sendNotificationEmail(String email, String displayName, String deliveryId, String status) {
		String subject = emailMessages.getMessage("notification.email.subject", null, Locale.US);
		String text = emailMessages.getMessage("notification.email.text",
				new Object[] { displayName, deliveryId, status }, locale);
		sendEmail(email, subject, text);

	}

	@Override
	public void sendNewOrderEmail(String email, String displayName, String deliveryId, String status) {
		String subject = emailMessages.getMessage("newpackage.email.subject", null, Locale.US);
		String text = emailMessages.getMessage("newpackage.email.text", new Object[] { displayName, deliveryId, status },
				locale);
		sendEmail(email, subject, text);
	}

	@Override
	public void sendThresholdExceededEmail(String email, String displayName, String deliveryId, String status) {
		String subject = emailMessages.getMessage("threshold.email.subject", null, Locale.US);
		String text = emailMessages.getMessage("threshold.email.text", new Object[] { displayName, deliveryId, status },
				locale);
		sendEmail(email, subject, text);

	}

	@Override
	public void sendThresholdExceededEmail(String email, String displayName, String[] deliveryId, String status) {
		String subject = emailMessages.getMessage("threshold.email.subject", null, Locale.US);
		String text = emailMessages.getMessage("threshold.email.text", new Object[] { displayName, deliveryId, status },
				locale);
		sendEmail(email, subject, text);

	}

	@Override
	public void sendFeedbackAddedEmail(Feedback fb) {
		String subject = "new feedback is added regarding account: " + fb.getSourceAccoundId();
		String text = fb.getText();
		sendEmail("dmitryli@outlook.com", subject, text);

	}

	
	private void sendEmail(String email, String subject, String text) {
		if (isSendEmailOn) {
			SimpleMailMessage mimeMessage = new SimpleMailMessage();
			mimeMessage.setTo(email);
			mimeMessage.setSubject(subject);
			mimeMessage.setText(text);
			mailSender.send(mimeMessage);
		} else {
			logger.info("Email is turned off");
		}

	}

}
