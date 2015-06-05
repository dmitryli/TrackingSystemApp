package com.dimalimonov.tracking.integration.tests.rest;

import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.RequestEntity.HeadersBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import com.dimalimonov.tracking.TrackingSystemApplication;
import com.dimalimonov.tracking.domain.Account;
import com.dimalimonov.tracking.domain.User;
import com.dimalimonov.tracking.integration.tests.util.TestsHelper;
import com.dimalimonov.tracking.util.PTrackIUrlService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TrackingSystemApplication.class)
@WebAppConfiguration
@ActiveProfiles("production")
public class RestRegistrationControllerIT {

	private static final Logger logger = LoggerFactory.getLogger(RestRegistrationControllerIT.class);

	@Autowired
	private PTrackIUrlService pTrackIUrlService = null;

	private RestTemplate template = new TestRestTemplate();

	private RestTemplate authorizedTemplate = new TestRestTemplate("dmitryli@outlook.com", "integration");

	private boolean isCleanup = true;
	@Test
	public void testRegisterNewMinimalCustomer() throws Exception {
		User c = new User();
		c.setDisplayName("My Test Customer");
		c.setEmail("dmitryli@outlook.com");
		c.setPassword("integration");

		ResponseEntity<List> response = template.postForEntity(pTrackIUrlService.getRegistratonURI(), c, List.class);
		Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());

		List<Map<String, String>> list = (List<Map<String, String>>) response.getBody();
		Map<String, String> accountMap = list.get(0);
		String accountLink = accountMap.get("href");
		logger.info("account {}", accountLink);
		ResponseEntity<Account> accountEntity = authorizedTemplate.getForEntity(accountLink, Account.class);

		Assert.assertNotNull(accountEntity.getBody());
		Assert.assertEquals(HttpStatus.OK, accountEntity.getStatusCode());

		Map<String, String> usersMap = list.get(1);
		String userLink = usersMap.get("href");
		logger.info("user {}", userLink);
		ResponseEntity<User> userEntity = authorizedTemplate.getForEntity(userLink, User.class);
		Assert.assertNotNull(userEntity.getBody());
		Assert.assertEquals(HttpStatus.OK, userEntity.getStatusCode());

		// Delete account and user
		if (isCleanup) {
			authorizedTemplate.delete(accountLink);
			authorizedTemplate.delete(userLink);
		}
		

	}

}
