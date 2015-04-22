package com.dimalimonov.tracking.integration.tests.rest;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import com.dimalimonov.tracking.TrackingSystemApplication;
import com.dimalimonov.tracking.domain.Account;
import com.dimalimonov.tracking.domain.User;
import com.dimalimonov.tracking.domain.Delivery;
import com.dimalimonov.tracking.domain.DeliveriesCollection;
import com.dimalimonov.tracking.util.Constants;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TrackingSystemApplication.class)
@WebAppConfiguration
@ActiveProfiles("production")

public class FullEndToEndControllerIT {

	private static final Logger logger = LoggerFactory.getLogger(FullEndToEndControllerIT.class);

	private RestTemplate template = new TestRestTemplate();


	@Value("classpath:usps-numbers.txt")
	private Resource uspsNumbers = null;
	
	@Test
	public void testEndToEnd() throws Exception {
		User c = new User();
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

		Map<String, String> userMap = list.get(1);
		String userLink = userMap.get("href");
		logger.info("user {}", userLink);
		ResponseEntity<User> userEntity = template.getForEntity(userLink, User.class);
		Assert.assertNotNull(userEntity.getBody());
		Assert.assertEquals(HttpStatus.OK, userEntity.getStatusCode());

		String deliveryUri = accountLink + "/deliveries";

		File file = uspsNumbers.getFile();
		List<String> lines = FileUtils.readLines(file);

		for (String s : lines) {
			Delivery o = new Delivery();
			o.setId(s);
			DeliveriesCollection collection = new DeliveriesCollection();
			collection.setDeliveries(Collections.singletonList(o));
			ResponseEntity<List> deliveryResponse = template.postForEntity(deliveryUri, collection, List.class);
			Assert.assertEquals(HttpStatus.CREATED, deliveryResponse.getStatusCode());
		}

		// Find created account
		Account found = template.getForObject(accountLink, Account.class);
		Assert.assertNotNull(found);
		
		// Delete account and user
		template.delete(accountLink);
		template.delete(userLink);

	}

}
