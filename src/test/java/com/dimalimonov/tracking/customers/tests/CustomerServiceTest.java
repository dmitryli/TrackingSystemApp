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
import com.dimalimonov.tracking.service.CustomerService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TrackingSystemApplication.class)
@WebAppConfiguration
public class CustomerServiceTest {

	private static final Logger logger = LoggerFactory.getLogger(CustomerServiceTest.class);

	@Autowired
	private CustomerService customerService = null;

	private Customer createSampleCustomer() {
		Customer customer = new Customer();
		customer.setDisplayName("test");
		customer.setEmail("test@test.com");
		customer.setPassword("test123");
		return customer;
	}

	@Test
	public void createCustomer() {
		Customer result = customerService.create(createSampleCustomer());
		Assert.assertNotNull(result.getId());
		customerService.deleteById(result.getId());
	}

	@Test
	public void findCustomerById() {
		Customer result = customerService.create(createSampleCustomer());
		Assert.assertNotNull(result.getId());
		Customer customerById = customerService.findById(result.getId());
		Assert.assertEquals(result.getDisplayName(), customerById.getDisplayName());
		Assert.assertEquals(result.getEmail(), customerById.getEmail());
		Assert.assertEquals(result.getPassword(), customerById.getPassword());

		customerService.deleteById(result.getId());
	}

	@Test
	public void findCustomerByEmail() {
		Customer result = customerService.create(createSampleCustomer());
		Assert.assertNotNull(result.getId());
		Customer customerByEmail = customerService.findByEmail(result.getEmail());
		Assert.assertNotNull(customerByEmail);
		Assert.assertEquals(result.getId(), customerByEmail.getId());
		Assert.assertEquals(result.getDisplayName(), customerByEmail.getDisplayName());
		Assert.assertEquals(result.getEmail(), customerByEmail.getEmail());
		Assert.assertEquals(result.getPassword(), customerByEmail.getPassword());

		customerService.deleteById(result.getId());
	}

	@Test
	public void updateCustomerDisplayName() {
		Customer result = customerService.create(createSampleCustomer());
		Assert.assertNotNull(result.getId());
		Customer customerByEmail = customerService.findByEmail(result.getEmail());
		Assert.assertNotNull(customerByEmail);

		customerByEmail.setDisplayName("NewDisplayName");
		customerService.update(customerByEmail);
		Customer updatedCustomer = customerService.findByEmail(result.getEmail());

		Assert.assertNotNull(customerByEmail);
		Assert.assertEquals("NewDisplayName", updatedCustomer.getDisplayName());
		customerService.deleteById(result.getId());
	}

	@Test
	public void updateCustomerPassword() {
		Customer result = customerService.create(createSampleCustomer());
		Assert.assertNotNull(result.getId());
		Customer customerByEmail = customerService.findByEmail(result.getEmail());
		Assert.assertNotNull(customerByEmail);

		customerByEmail.setPassword("NewPassword");
		customerService.update(customerByEmail);
		Customer updatedCustomer = customerService.findByEmail(result.getEmail());

		Assert.assertNotNull(customerByEmail);
		Assert.assertEquals("NewPassword", updatedCustomer.getPassword());
		customerService.deleteById(result.getId());
	}

	@Test
	public void deleteCustomerById() {
		Customer result = customerService.create(createSampleCustomer());
		Assert.assertNotNull(result.getId());
		customerService.deleteById(result.getId());
		Customer customerById = customerService.findById(result.getId());
		Assert.assertNull(customerById);
	}
}
