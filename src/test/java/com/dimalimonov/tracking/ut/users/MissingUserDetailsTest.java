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
import com.dimalimonov.tracking.errors.MissingUserDetailsException;
import com.dimalimonov.tracking.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TrackingSystemApplication.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class MissingUserDetailsTest {

	private static final Logger logger = LoggerFactory.getLogger(MissingUserDetailsTest.class);

	@Autowired
	private UserService userService = null;

	@Test
	public void createInvaliduserNoDisplayName() {
		User user = new User();
		user.setEmail("test@test.com");
		user.setPassword("test123");
		User result = null;
		try {
			result = userService.create(user);
			Assert.assertNull(result);
		} catch (MissingUserDetailsException e) {
			logger.error(e.getMessage());
			Assert.assertNull(result);
			Assert.assertEquals("CUST_ERR_0002: Missing field(s) are: Display Name", e.getMessage());
		}

	}

	@Test
	public void createInvaliduserNoEmail() {
		User user = new User();
		user.setDisplayName("test");
		user.setPassword("test123");
		User result = null;
		try {
			result = userService.create(user);
			Assert.assertNull(result);
		} catch (MissingUserDetailsException e) {
			logger.error(e.getMessage());
			Assert.assertNull(result);
			Assert.assertEquals("CUST_ERR_0002: Missing field(s) are: Email", e.getMessage());
		}

	}

	@Test
	public void createInvaliduserNoPassword() {
		User user = new User();
		user.setDisplayName("test");
		user.setEmail("test@test.com");
		User result = null;
		try {
			result = userService.create(user);
			Assert.assertNull(result);
		} catch (MissingUserDetailsException e) {
			logger.error(e.getMessage());
			Assert.assertNull(result);
			Assert.assertEquals("CUST_ERR_0002: Missing field(s) are: Password", e.getMessage());
		}
	}

	@Test
	public void createInvaliduserNoPasswordNoEmail() {
		User user = new User();
		user.setDisplayName("test");
		User result = null;
		try {
			result = userService.create(user);
			Assert.assertNull(result);
		} catch (MissingUserDetailsException e) {
			logger.error(e.getMessage());
			Assert.assertNull(result);
			Assert.assertEquals("CUST_ERR_0002: Missing field(s) are: Email, Password", e.getMessage());
		}
	}

	@Test
	public void createInvaliduserNoPasswordNoDisplayName() {
		User user = new User();
		user.setEmail("test@test.com");
		User result = null;
		try {
			result = userService.create(user);
			Assert.assertNull(result);
		} catch (MissingUserDetailsException e) {
			logger.error(e.getMessage());
			Assert.assertNull(result);
			Assert.assertEquals("CUST_ERR_0002: Missing field(s) are: Display Name, Password", e.getMessage());
		}
	}

	@Test
	public void createInvaliduserNoEmailNoDisplayName() {
		User user = new User();
		user.setPassword("test123");
		User result = null;
		try {
			result = userService.create(user);
			Assert.assertNull(result);
		} catch (MissingUserDetailsException e) {
			logger.error(e.getMessage());
			Assert.assertNull(result);
			Assert.assertEquals("CUST_ERR_0002: Missing field(s) are: Display Name, Email", e.getMessage());
		}
	}
}
