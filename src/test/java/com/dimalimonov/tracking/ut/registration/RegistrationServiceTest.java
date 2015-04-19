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
import com.dimalimonov.tracking.domain.Customer;
import com.dimalimonov.tracking.domain.RegistrationResult;
import com.dimalimonov.tracking.service.AccountOrderService;
import com.dimalimonov.tracking.service.CustomerService;
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
	private CustomerService customerService = null;

	@Autowired
	private AccountOrderService accountService = null;

	private Customer createSampleCustomer() {
		Customer customer = new Customer();
		customer.setDisplayName("test");
		customer.setEmail("test@test.com");
		customer.setPassword("test123");
		return customer;
	}

	@Test
	public void registerCustomer() {
		RegistrationResult register = registrationService.register(createSampleCustomer());
		Assert.assertNotNull(register);
		Assert.assertNotNull(register.getAccount());
		Assert.assertNotNull(register.getAccount().getId());
		Assert.assertNotNull(register.getCustomer().getId());

		Assert.assertEquals(register.getCustomer().getAccountId(), register.getAccount().getId());

		accountService.deleteAccount(register.getAccount().getId());
		customerService.deleteById(register.getCustomer().getId());
	}

}
