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
import com.dimalimonov.tracking.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TrackingSystemApplication.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class UserServiceTest {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceTest.class);

	@Autowired
	private UserService userService = null;

	private User createSampleUser() {
		User user = new User();
		user.setDisplayName("test");
		user.setEmail("test@test.com");
		user.setPassword("test123");
		return user;
	}

	@Test
	public void createUser() {
		User result = userService.create(createSampleUser());
		Assert.assertNotNull(result.getId());
		userService.deleteById(result.getId());
	}

	@Test
	public void findUserById() {
		User result = userService.create(createSampleUser());
		Assert.assertNotNull(result.getId());
		User userById = userService.findById(result.getId());
		Assert.assertEquals(result.getDisplayName(), userById.getDisplayName());
		Assert.assertEquals(result.getEmail(), userById.getEmail());
		Assert.assertEquals(result.getPassword(), userById.getPassword());

		userService.deleteById(result.getId());
	}

	@Test
	public void findUserByEmail() {
		User result = userService.create(createSampleUser());
		Assert.assertNotNull(result.getId());
		User userByEmail = userService.findByEmail(result.getEmail());
		Assert.assertNotNull(userByEmail);
		Assert.assertEquals(result.getId(), userByEmail.getId());
		Assert.assertEquals(result.getDisplayName(), userByEmail.getDisplayName());
		Assert.assertEquals(result.getEmail(), userByEmail.getEmail());
		Assert.assertEquals(result.getPassword(), userByEmail.getPassword());

		userService.deleteById(result.getId());
	}

	@Test
	public void updateUserDisplayName() {
		User result = userService.create(createSampleUser());
		Assert.assertNotNull(result.getId());
		User userByEmail = userService.findByEmail(result.getEmail());
		Assert.assertNotNull(userByEmail);

		userByEmail.setDisplayName("NewDisplayName");
		userService.update(userByEmail);
		User updatedCustomer = userService.findByEmail(result.getEmail());

		Assert.assertNotNull(userByEmail);
		Assert.assertEquals("NewDisplayName", updatedCustomer.getDisplayName());
		userService.deleteById(result.getId());
	}

	@Test
	public void updateUserPassword() {
		User result = userService.create(createSampleUser());
		Assert.assertNotNull(result.getId());
		User userByEmail = userService.findByEmail(result.getEmail());
		Assert.assertNotNull(userByEmail);

		userByEmail.setPassword("NewPassword");
		userService.update(userByEmail);
		User updatedCustomer = userService.findByEmail(result.getEmail());

		Assert.assertNotNull(userByEmail);
		Assert.assertEquals("NewPassword", updatedCustomer.getPassword());
		userService.deleteById(result.getId());
	}

	@Test
	public void deleteUserById() {
		User result = userService.create(createSampleUser());
		Assert.assertNotNull(result.getId());
		userService.deleteById(result.getId());
		User userById = userService.findById(result.getId());
		Assert.assertNull(userById);
	}
}
