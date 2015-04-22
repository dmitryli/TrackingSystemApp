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
import com.dimalimonov.tracking.service.AccountDeliveriesService;
import com.dimalimonov.tracking.service.UserService;
import com.dimalimonov.tracking.service.RegistrationService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TrackingSystemApplication.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class RegistrationServiceTest {

	private static final Logger logger = LoggerFactory.getLogger(RegistrationServiceTest.class);

	@Autowired
	private RegistrationService registrationService = null;

	@Autowired
	private UserService userService = null;

	@Autowired
	private AccountDeliveriesService accountService = null;

	private User createSampleCustomer() {
		User user = new User();
		user.setDisplayName("test");
		user.setEmail("test@test.com");
		user.setPassword("test123");
		return user;
	}

	@Test
	public void registerCustomer() {
		RegistrationResult register = registrationService.register(createSampleCustomer());
		Assert.assertNotNull(register);
		Assert.assertNotNull(register.getAccount());
		Assert.assertNotNull(register.getAccount().getId());
		Assert.assertNotNull(register.getUser().getId());

		Assert.assertEquals(register.getUser().getAccountId(), register.getAccount().getId());

		accountService.deleteAccount(register.getAccount().getId());
		userService.deleteById(register.getUser().getId());
	}

}
