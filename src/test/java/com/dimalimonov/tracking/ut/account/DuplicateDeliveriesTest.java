package com.dimalimonov.tracking.ut.account;

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
import com.dimalimonov.tracking.errors.DuplicateDeliveryException;
import com.dimalimonov.tracking.service.AccountDeliveriesService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TrackingSystemApplication.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class DuplicateDeliveriesTest {

	private static final Logger logger = LoggerFactory.getLogger(DuplicateDeliveriesTest.class);
	@Value("${usps.threshold}")
	public Integer uspsThreshold = null;

	@Value("${ups.threshold}")
	public Integer upsThreshold = null;

	@Autowired
	private AccountDeliveriesService accountOrderService = null;

	@Test
	public void createDuplicateOrder() {
		Account account = new Account();
		account = accountOrderService.createAccount(account);
		Assert.assertNotNull(account.getId());

		Delivery o = new Delivery();
		o.setCarrier(Carrier.USPS);
		o.setId("9405903699300380069915");

		accountOrderService.createDelivery(account.getId(), o);

		o = new Delivery();
		o.setCarrier(Carrier.USPS);
		o.setId("9405903699300380069915");

		try {
			Delivery duplicateOrder = accountOrderService.createDelivery(account.getId(), o);
			Assert.assertNull(duplicateOrder);
		} catch (DuplicateDeliveryException e) {
			logger.error(e.getMessage());
			Assert.assertEquals("PACK_ERR_0004: Package with id 9405903699300380069915 already exists in this account",
					e.getMessage());
		} finally {
			accountOrderService.deleteAccount(account.getId());
		}

	}

	@Test
	public void createDuplicateOrderWhenFewExists() {
		Account account = new Account();
		account = accountOrderService.createAccount(account);
		Assert.assertNotNull(account.getId());

		Delivery o = new Delivery();
		o.setCarrier(Carrier.USPS);
		o.setId("9405903699300380069915");

		accountOrderService.createDelivery(account.getId(), o);

		o = new Delivery();
		o.setCarrier(Carrier.USPS);
		o.setId("9405903699300367183443");

		accountOrderService.createDelivery(account.getId(), o);

		o = new Delivery();
		o.setCarrier(Carrier.USPS);
		o.setId("9405903699300368436531");

		accountOrderService.createDelivery(account.getId(), o);

		o = new Delivery();
		o.setCarrier(Carrier.USPS);
		o.setId("9405903699300380069915");

		try {
			Delivery duplicateOrder = accountOrderService.createDelivery(account.getId(), o);
			Assert.assertNull(duplicateOrder);
		} catch (DuplicateDeliveryException e) {
			logger.error(e.getMessage());
			Assert.assertEquals("PACK_ERR_0004: Package with id 9405903699300380069915 already exists in this account",
					e.getMessage());
		} finally {
			accountOrderService.deleteAccount(account.getId());
		}

	}
}
