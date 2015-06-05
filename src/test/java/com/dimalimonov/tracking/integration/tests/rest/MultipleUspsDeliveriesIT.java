package com.dimalimonov.tracking.integration.tests.rest;

import java.io.File;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.dimalimonov.tracking.domain.DeliveriesCollection;
import com.dimalimonov.tracking.domain.Delivery;
import com.dimalimonov.tracking.util.PTrackIUrlService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TrackingSystemApplication.class)
@WebAppConfiguration
@ActiveProfiles("production")
public class MultipleUspsDeliveriesIT {

	private static final Logger logger = LoggerFactory.getLogger(MultipleUspsDeliveriesIT.class);

	@Autowired
	private PTrackIUrlService pTrackIUrlService = null;

	private RestTemplate template = new TestRestTemplate();

	private RestTemplate authorizedTemplate = new TestRestTemplate("dmitryli@outlook.com", "integration");

	@Value("classpath:usps-numbers.txt")
	private Resource uspsNumbers = null;

	@Ignore
	@Test
	public void testCreateReadDeleteAccountWithManyUSPSOrders() throws Exception {
		Account a = new Account();

		// Create brand new account
		ResponseEntity<Account> response = template.postForEntity(pTrackIUrlService.getAccountsURI(), a, Account.class);

		Assert.assertNotNull(response.getHeaders().getLocation());
		String accountUri = response.getHeaders().getLocation().toString();

		String deliveryUri = accountUri + "/deliveries";

		File file = uspsNumbers.getFile();
		List<String> lines = FileUtils.readLines(file);

		for (String s : lines) {
			Delivery o = new Delivery();
			o.setId(s);
			DeliveriesCollection collection = new DeliveriesCollection();
			collection.setDeliveries(Collections.singletonList(o));
			ResponseEntity<List> deliveryResponse = template.postForEntity(deliveryUri, collection, List.class);
			Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());
		}

		// Find created account
		Account found = template.getForObject(accountUri, Account.class);
		Assert.assertNotNull(found);

		// Delete account
		template.delete(accountUri);

	}

}
