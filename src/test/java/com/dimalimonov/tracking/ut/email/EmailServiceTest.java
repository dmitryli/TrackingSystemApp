package com.dimalimonov.tracking.ut.email;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.dimalimonov.tracking.TrackingSystemApplication;
import com.dimalimonov.tracking.domain.User;
import com.dimalimonov.tracking.service.EmailService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TrackingSystemApplication.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class EmailServiceTest {

	private static final Logger logger = LoggerFactory.getLogger(EmailServiceTest.class);

	@Autowired
	private EmailService emailService = null;



	@Test
	public void sendEmail() {
		emailService.sendWelcomeEmail("dmitryli@gmail.com", "dimka", "111");
	}

	
}
