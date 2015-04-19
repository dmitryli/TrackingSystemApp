package com.dimalimonov.tracking.integration.tests.rest;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import com.dimalimonov.tracking.TrackingSystemApplication;
import com.dimalimonov.tracking.domain.Account;
import com.dimalimonov.tracking.util.Constants;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TrackingSystemApplication.class)
@WebAppConfiguration
@ActiveProfiles("production")
public class RestAccountControllerIT {

	private static final Logger logger = LoggerFactory.getLogger(RestAccountControllerIT.class);

	private RestTemplate template = new TestRestTemplate();

	@Test
	public void testCreateReadDeleteAccount() throws Exception {
		Account a = new Account();

		// Create brand new account
		ResponseEntity<Account> response = template.postForEntity(Constants.BASE_ACCOUNT_URI, a, Account.class);

		Assert.assertNotNull(response.getHeaders().getLocation());
		String uri = response.getHeaders().getLocation().toString();
		logger.info("Account created at {}", uri);

		Account body = response.getBody();
		Assert.assertNotNull(body);

		String composedUri = Constants.BASE_ACCOUNT_URI + "/" + body.getId();
		Assert.assertEquals(composedUri, uri);
		
		// Find created account
		Account found = template.getForObject(uri, Account.class);
		Assert.assertEquals(body.getId(), found.getId());

		// Delete account
		template.delete(uri);
		
	}

}
