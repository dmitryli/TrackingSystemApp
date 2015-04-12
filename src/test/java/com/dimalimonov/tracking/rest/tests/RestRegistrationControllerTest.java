package com.dimalimonov.tracking.rest.tests;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import com.dimalimonov.tracking.TrackingSystemApplication;
import com.dimalimonov.tracking.domain.Account;
import com.dimalimonov.tracking.domain.Customer;
import com.dimalimonov.tracking.util.Constants;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TrackingSystemApplication.class)
@WebAppConfiguration
public class RestRegistrationControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(RestRegistrationControllerTest.class);

	private RestTemplate template = new RestTemplate();

	@Ignore
	@Test
	public void testRequest() throws Exception {
		Customer customer = new Customer();
		customer.setDisplayName("test");
		customer.setEmail("test@test.com");
		customer.setPassword("test123");
		ResponseEntity<Account> response = template.postForEntity(Constants.REGISTRATION_URI, customer, Account.class);

		String location = response.getHeaders().getLocation().toString();
		logger.info(location);

	}

}
