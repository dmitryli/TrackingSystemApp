package com.dimalimonov.tracking.integration.tests.rest;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
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
import com.dimalimonov.tracking.domain.Carrier;
import com.dimalimonov.tracking.domain.DeliveriesCollection;
import com.dimalimonov.tracking.domain.Delivery;
import com.dimalimonov.tracking.domain.DeliveryState;
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

		Delivery o = new Delivery();
		o.setCarrier(Carrier.USPS);
		o.setId("9405903699300380069915");
		o.setDescription("My favorite Order");

		DeliveriesCollection collection = new DeliveriesCollection();
		collection.setDeliveries(Collections.singletonList(o));

		String deliveriesUri = uri + "/deliveries";

		ResponseEntity<List> deliveriesResponse = template.postForEntity(deliveriesUri, collection, List.class);
		Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());

		List<Map<String, String>> deliveries = (List<Map<String, String>>) deliveriesResponse.getBody();
		Map<String, String> delivery = deliveries.get(0);

		logger.info("location {}", response.getHeaders().getLocation());

		Assert.assertEquals("9405903699300380069915", delivery.get("id"));
		Assert.assertEquals("USPS", delivery.get("carrier"));
		Assert.assertEquals("My favorite Order", delivery.get("description"));

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
	
	@Test
	public void testFindDeliveriesByState() throws Exception {
		Account a = new Account();

		// Create brand new account
		ResponseEntity<Account> response = template.postForEntity(Constants.BASE_ACCOUNT_URI, a, Account.class);

		Assert.assertNotNull(response.getHeaders().getLocation());
		String uri = response.getHeaders().getLocation().toString();

		List<Delivery> list = new LinkedList<Delivery>();
		
		Delivery o = new Delivery();
		o.setCarrier(Carrier.USPS);
		o.setId("9405903699300380069915");
		o.setDescription("My favorite Order");
		
		list.add(o);
		
		o = new Delivery();
		o.setCarrier(Carrier.USPS);
		o.setId("9405903699300372857186");
		list.add(o);

		DeliveriesCollection collection = new DeliveriesCollection();
		collection.setDeliveries(list);

		String deliveriesUri = uri + "/deliveries";

		ResponseEntity<List> deliveriesResponse = template.postForEntity(deliveriesUri, collection, List.class);
		Assert.assertEquals(HttpStatus.CREATED, deliveriesResponse.getStatusCode());
		
		
		// Archive one delivery
		o.setState(DeliveryState.ARCHIVED);
		template.postForLocation(deliveriesUri + "/9405903699300372857186/state", o);

		//find active deliveries
		String activeUri = deliveriesUri+"?active";
		ResponseEntity<List> actviveDeliveries = template.getForEntity(activeUri, List.class);
		Assert.assertEquals(1, actviveDeliveries.getBody().size());

		
		// find archived deliveries
		String archived = deliveriesUri+"?archived";
		ResponseEntity<List> archivedDeliveries = template.getForEntity(archived, List.class);
		Assert.assertEquals(1, archivedDeliveries.getBody().size());
		
		// Delete account
		template.delete(uri);

	}

}
