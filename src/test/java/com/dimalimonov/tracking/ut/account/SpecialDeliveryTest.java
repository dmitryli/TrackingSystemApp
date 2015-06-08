package com.dimalimonov.tracking.ut.account;

import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.dimalimonov.tracking.TrackingSystemApplication;
import com.dimalimonov.tracking.domain.Account;
import com.dimalimonov.tracking.domain.Carrier;
import com.dimalimonov.tracking.domain.Delivery;
import com.dimalimonov.tracking.domain.DeliveryState;
import com.dimalimonov.tracking.service.AccountDeliveriesService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TrackingSystemApplication.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class SpecialDeliveryTest {

	private static final Logger logger = LoggerFactory.getLogger(SpecialDeliveryTest.class);
	@Value("${usps.threshold}")
	public Integer uspsThreshold = null;

	@Value("${ups.threshold}")
	public Integer upsThreshold = null;

	@Autowired
	private AccountDeliveriesService accountOrderService = null;

	@Test
	public void createOneDelivery() {
		
		String orderId = "9274890104201934599884";
		Carrier carrier = Carrier.UPS;
		
		Account account = new Account();
		account = accountOrderService.createAccount(account);
		Assert.assertNotNull(account.getId());

		Delivery o = new Delivery();
		o.setCarrier(carrier);
		o.setId(orderId);


		accountOrderService.createDelivery(account.getId(), o);

		Account find = accountOrderService.findAccount(account.getId());
		Assert.assertNotNull(find);
		Assert.assertNotNull(find.getDeliveries());
		Assert.assertTrue(find.getDeliveries().size() == 1);

		Delivery result = find.getDeliveries().get(0);
		Assert.assertTrue(result.getId().equals(orderId));
		Assert.assertTrue(result.getCarrier().equals(carrier));
		Assert.assertEquals(result.getThreshold(), uspsThreshold.intValue());
		Assert.assertEquals(result.getState(), DeliveryState.ACTIVE);

		accountOrderService.deleteAccount(account.getId());

	}

	
}
