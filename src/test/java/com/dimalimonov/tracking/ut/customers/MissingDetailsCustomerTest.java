package com.dimalimonov.tracking.ut.customers;

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
import com.dimalimonov.tracking.errors.MissingCustomerDetailsException;
import com.dimalimonov.tracking.service.CustomerService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TrackingSystemApplication.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class MissingDetailsCustomerTest {

	private static final Logger logger = LoggerFactory.getLogger(MissingDetailsCustomerTest.class);

	@Autowired
	private CustomerService customerService = null;

	@Test
	public void createInvalidCustomerNoDisplayName() {
		Customer customer = new Customer();
		customer.setEmail("test@test.com");
		customer.setPassword("test123");
		Customer result = null;
		try {
			result = customerService.create(customer);
			Assert.assertNull(result);
		} catch (MissingCustomerDetailsException e) {
			logger.error(e.getMessage());
			Assert.assertNull(result);
			Assert.assertEquals("CUST_ERR_0002: Missing field(s) are: Display Name", e.getMessage());
		}

	}

	@Test
	public void createInvalidCustomerNoEmail() {
		Customer customer = new Customer();
		customer.setDisplayName("test");
		customer.setPassword("test123");
		Customer result = null;
		try {
			result = customerService.create(customer);
			Assert.assertNull(result);
		} catch (MissingCustomerDetailsException e) {
			logger.error(e.getMessage());
			Assert.assertNull(result);
			Assert.assertEquals("CUST_ERR_0002: Missing field(s) are: Email", e.getMessage());
		}

	}

	@Test
	public void createInvalidCustomerNoPassword() {
		Customer customer = new Customer();
		customer.setDisplayName("test");
		customer.setEmail("test@test.com");
		Customer result = null;
		try {
			result = customerService.create(customer);
			Assert.assertNull(result);
		} catch (MissingCustomerDetailsException e) {
			logger.error(e.getMessage());
			Assert.assertNull(result);
			Assert.assertEquals("CUST_ERR_0002: Missing field(s) are: Password", e.getMessage());
		}
	}

	@Test
	public void createInvalidCustomerNoPasswordNoEmail() {
		Customer customer = new Customer();
		customer.setDisplayName("test");
		Customer result = null;
		try {
			result = customerService.create(customer);
			Assert.assertNull(result);
		} catch (MissingCustomerDetailsException e) {
			logger.error(e.getMessage());
			Assert.assertNull(result);
			Assert.assertEquals("CUST_ERR_0002: Missing field(s) are: Email, Password", e.getMessage());
		}
	}

	@Test
	public void createInvalidCustomerNoPasswordNoDisplayName() {
		Customer customer = new Customer();
		customer.setEmail("test@test.com");
		Customer result = null;
		try {
			result = customerService.create(customer);
			Assert.assertNull(result);
		} catch (MissingCustomerDetailsException e) {
			logger.error(e.getMessage());
			Assert.assertNull(result);
			Assert.assertEquals("CUST_ERR_0002: Missing field(s) are: Display Name, Password", e.getMessage());
		}
	}

	@Test
	public void createInvalidCustomerNoEmailNoDisplayName() {
		Customer customer = new Customer();
		customer.setPassword("test123");
		Customer result = null;
		try {
			result = customerService.create(customer);
			Assert.assertNull(result);
		} catch (MissingCustomerDetailsException e) {
			logger.error(e.getMessage());
			Assert.assertNull(result);
			Assert.assertEquals("CUST_ERR_0002: Missing field(s) are: Display Name, Email", e.getMessage());
		}
	}
}
