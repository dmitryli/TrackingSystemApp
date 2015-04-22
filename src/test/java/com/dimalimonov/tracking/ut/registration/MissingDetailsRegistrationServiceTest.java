package com.dimalimonov.tracking.ut.registration;

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
import com.dimalimonov.tracking.domain.RegistrationResult;
import com.dimalimonov.tracking.errors.MissingUserDetailsException;
import com.dimalimonov.tracking.service.UserService;
import com.dimalimonov.tracking.service.RegistrationService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TrackingSystemApplication.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class MissingDetailsRegistrationServiceTest {

	private static final Logger logger = LoggerFactory.getLogger(MissingDetailsRegistrationServiceTest.class);

	@Autowired
	private RegistrationService registrationService = null;

	@Autowired
	private UserService userService = null;

	@Test
	public void registerCustomer() {
		User user = new User();
		user.setDisplayName("test");
		user.setEmail("test@test.com");
		RegistrationResult register = null;
		try {
			register = registrationService.register(user);
			Assert.assertNull(register);
		} catch (MissingUserDetailsException e) {
			logger.error(e.getMessage());
			Assert.assertNull(register);
			Assert.assertEquals("CUST_ERR_0002: Missing field(s) are: Password", e.getMessage());
		}

	}
}
