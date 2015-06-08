package com.dimalimonov.tracking.integration.tests.rest;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
import com.dimalimonov.tracking.domain.Carrier;
import com.dimalimonov.tracking.domain.DeliveriesCollection;
import com.dimalimonov.tracking.domain.Delivery;
import com.dimalimonov.tracking.domain.DeliveryState;
import com.dimalimonov.tracking.util.PTrackIUrlService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TrackingSystemApplication.class)
@WebAppConfiguration
@ActiveProfiles("production")
public class SpecialDeliveryControllerIT {

	private static final Logger logger = LoggerFactory.getLogger(SpecialDeliveryControllerIT.class);

	@Autowired
	private PTrackIUrlService pTrackIUrlService = null;

	private RestTemplate template = new TestRestTemplate();

	private RestTemplate authorizedTemplate = new TestRestTemplate("dmitryli@outlook.com", "integration");

	@Value("classpath:usps-numbers.txt")
	private Resource uspsNumbers = null;

	@Test
	public void testCreateReadDeleteAccountWithOrders() throws Exception {
		Account a = new Account();

		// Create brand new account
		ResponseEntity<Account> response = template.postForEntity(pTrackIUrlService.getAccountsURI(), a, Account.class);

		Assert.assertNotNull(response.getHeaders().getLocation());
		String uri = response.getHeaders().getLocation().toString();

		Delivery o = new Delivery();
		o.setCarrier(Carrier.UPS);
		o.setId("9274890104201934599884");

		o.setThreshold(48);

		DeliveriesCollection collection = new DeliveriesCollection();
		collection.setDeliveries(Collections.singletonList(o));

		String deliveriesUri = uri + "/deliveries";

		ResponseEntity<List> deliveriesResponse = template.postForEntity(deliveriesUri, collection, List.class);
		Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());

		List<Map<String, String>> deliveries = (List<Map<String, String>>) deliveriesResponse.getBody();
		Map<String, String> delivery = deliveries.get(0);

		logger.info("location {}", response.getHeaders().getLocation());

		Assert.assertEquals("9274890104201934599884", delivery.get("id"));
		Assert.assertEquals("UPS", delivery.get("carrier"));
		Assert.assertEquals(48, delivery.get("threshold"));

		// Delete account
		template.delete(uri);

	}
	


}
