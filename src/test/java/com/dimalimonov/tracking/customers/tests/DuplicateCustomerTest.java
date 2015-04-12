package com.dimalimonov.tracking.customers.tests;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.dimalimonov.tracking.TrackingSystemApplication;
import com.dimalimonov.tracking.domain.Customer;
import com.dimalimonov.tracking.errors.DuplicateCustomerException;
import com.dimalimonov.tracking.service.CustomerService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TrackingSystemApplication.class)
@WebAppConfiguration
public class DuplicateCustomerTest {

	private static final Logger logger = LoggerFactory.getLogger(DuplicateCustomerTest.class);

	@Autowired
	private CustomerService customerService = null;

	@Test
	public void attemptCreateDuplicateCustomer() {
		Customer result = customerService.create(createSampleCustomerForDuplicationTest());
		Assert.assertNotNull(result.getId());
		try {
			Customer dup = customerService.create(createSampleCustomerForDuplicationTest());
			Assert.assertNull(dup);
		} catch (DuplicateCustomerException e) {
			logger.error(e.getMessage());
			Assert.assertEquals("CUST_ERR_0001: Customer with email duptest@test.com already exists", e.getMessage());
		} finally {
			customerService.deleteById(result.getId());
		}

	}

	private Customer createSampleCustomerForDuplicationTest() {
		Customer customer = new Customer();
		customer.setDisplayName("duptest");
		customer.setEmail("duptest@test.com");
		customer.setPassword("test123");
		return customer;
	}
}
