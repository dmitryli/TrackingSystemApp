package com.dimalimonov.tracking.ut.users;

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
import com.dimalimonov.tracking.errors.DuplicateUserException;
import com.dimalimonov.tracking.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TrackingSystemApplication.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class DuplicateUserTest {

	private static final Logger logger = LoggerFactory.getLogger(DuplicateUserTest.class);

	@Autowired
	private UserService userService = null;

	@Test
	public void attemptCreateDuplicateCustomer() {
		User result = userService.create(createSampleCustomerForDuplicationTest());
		Assert.assertNotNull(result.getId());
		try {
			User dup = userService.create(createSampleCustomerForDuplicationTest());
			Assert.assertNull(dup);
		} catch (DuplicateUserException e) {
			logger.error(e.getMessage());
			Assert.assertEquals("CUST_ERR_0001: Customer with email duptest@test.com already exists", e.getMessage());
		} finally {
			userService.deleteById(result.getId());
		}

	}

	private User createSampleCustomerForDuplicationTest() {
		User user = new User();
		user.setDisplayName("duptest");
		user.setEmail("duptest@test.com");
		user.setPassword("test123");
		return user;
	}
}
