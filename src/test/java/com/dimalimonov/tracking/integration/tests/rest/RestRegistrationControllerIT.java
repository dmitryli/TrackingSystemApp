package com.dimalimonov.tracking.integration.tests.rest;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import com.dimalimonov.tracking.TrackingSystemApplication;
import com.dimalimonov.tracking.domain.Account;
import com.dimalimonov.tracking.domain.Customer;
import com.dimalimonov.tracking.domain.Link;
import com.dimalimonov.tracking.util.Constants;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TrackingSystemApplication.class)
@WebAppConfiguration
@ActiveProfiles("production")
public class RestRegistrationControllerIT {

	private static final Logger logger = LoggerFactory.getLogger(RestRegistrationControllerIT.class);

	private RestTemplate template = new TestRestTemplate();

	@Test
	public void testRegisterNewMinimalCustomer() throws Exception {
		Customer c = new Customer();
		c.setDisplayName("My Test Customer");
		c.setEmail("dmitryli@outlook.com");
		c.setPassword("integration");

		ResponseEntity<List> response = template.postForEntity(Constants.REGISTRATION_URI, c, List.class);
		Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());

		List<Map<String, String>> list = (List<Map<String, String>>) response.getBody();
		Map<String, String> accountMap = list.get(0);
		String accountLink = accountMap.get("href");
		logger.info("account {}", accountLink);

		ResponseEntity<Account> accountEntity = template.getForEntity(accountLink, Account.class);
		Assert.assertNotNull(accountEntity.getBody());
		Assert.assertEquals(HttpStatus.OK, accountEntity.getStatusCode());

		Map<String, String> customerMap = list.get(1);
		String customerLink = customerMap.get("href");
		logger.info("customer {}", customerLink);
		ResponseEntity<Customer> customerEntity = template.getForEntity(customerLink, Customer.class);
		Assert.assertNotNull(customerEntity.getBody());
		Assert.assertEquals(HttpStatus.OK, customerEntity.getStatusCode());

		// Delete account and customer
		template.delete(accountLink);
		template.delete(customerLink);

	}

}
