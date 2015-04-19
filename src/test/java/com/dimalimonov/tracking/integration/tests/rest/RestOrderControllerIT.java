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
import com.dimalimonov.tracking.domain.Activity;
import com.dimalimonov.tracking.domain.Carrier;
import com.dimalimonov.tracking.domain.Order;
import com.dimalimonov.tracking.domain.OrderCollection;
import com.dimalimonov.tracking.util.Constants;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TrackingSystemApplication.class)
@WebAppConfiguration
@ActiveProfiles("production")
public class RestOrderControllerIT {

	private static final Logger logger = LoggerFactory.getLogger(RestOrderControllerIT.class);

	private RestTemplate template = new TestRestTemplate();

	@Value("classpath:usps-numbers.txt")
	private Resource uspsNumbers = null;

	@Test
	public void testCreateReadDeleteAccountWithOrders() throws Exception {
		Account a = new Account();

		// Create brand new account
		ResponseEntity<Account> response = template.postForEntity(Constants.BASE_ACCOUNT_URI, a, Account.class);

		Assert.assertNotNull(response.getHeaders().getLocation());
		String uri = response.getHeaders().getLocation().toString();

		Order o = new Order();
		o.setCarrier(Carrier.USPS);
		o.setId("9405903699300380069915");
		o.setDescription("My favorite Order");

		OrderCollection collection = new OrderCollection();
		collection.setOrders(Collections.singletonList(o));

		String ordersUri = uri + "/orders";

		ResponseEntity<List> ordersResponse = template.postForEntity(ordersUri, collection, List.class);
		Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());

		List<Map<String, String>> orders = (List<Map<String, String>>) ordersResponse.getBody();
		Map<String, String> order = orders.get(0);

		logger.info("location {}", response.getHeaders().getLocation());

		Assert.assertEquals("9405903699300380069915", order.get("id"));
		Assert.assertEquals("USPS", order.get("carrier"));
		Assert.assertEquals("My favorite Order", order.get("description"));

		// Delete account
		template.delete(uri);

	}

	@Test
	public void testCreateReadDeleteAccountWithManyUSPSOrders() throws Exception {
		Account a = new Account();

		// Create brand new account
		ResponseEntity<Account> response = template.postForEntity(Constants.BASE_ACCOUNT_URI, a, Account.class);

		Assert.assertNotNull(response.getHeaders().getLocation());
		String accountUri = response.getHeaders().getLocation().toString();

		String ordersUri = accountUri + "/orders";

		File file = uspsNumbers.getFile();
		List<String> lines = FileUtils.readLines(file);

		for (String s : lines) {
			Order o = new Order();
			o.setId(s);
			OrderCollection collection = new OrderCollection();
			collection.setOrders(Collections.singletonList(o));
			ResponseEntity<List> ordersResponse = template.postForEntity(ordersUri, collection, List.class);
			Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());
		}

		// Find created account
		Account found = template.getForObject(accountUri, Account.class);
		Assert.assertNotNull(found);

		// Delete account
		template.delete(accountUri);

	}

}
