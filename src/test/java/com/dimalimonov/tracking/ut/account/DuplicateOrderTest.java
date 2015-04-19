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
import com.dimalimonov.tracking.domain.Order;
import com.dimalimonov.tracking.errors.DuplicateOrderException;
import com.dimalimonov.tracking.service.AccountOrderService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TrackingSystemApplication.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class DuplicateOrderTest {

	private static final Logger logger = LoggerFactory.getLogger(DuplicateOrderTest.class);
	@Value("${usps.threshold}")
	public Integer uspsThreshold = null;

	@Value("${ups.threshold}")
	public Integer upsThreshold = null;

	@Autowired
	private AccountOrderService accountOrderService = null;

	@Test
	public void createDuplicateOrder() {
		Account account = new Account();
		account = accountOrderService.createAccount(account);
		Assert.assertNotNull(account.getId());

		Order o = new Order();
		o.setCarrier(Carrier.USPS);
		o.setId("9405903699300380069915");

		accountOrderService.createOrder(account.getId(), o);

		o = new Order();
		o.setCarrier(Carrier.USPS);
		o.setId("9405903699300380069915");

		try {
			Order duplicateOrder = accountOrderService.createOrder(account.getId(), o);
			Assert.assertNull(duplicateOrder);
		} catch (DuplicateOrderException e) {
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

		Order o = new Order();
		o.setCarrier(Carrier.USPS);
		o.setId("9405903699300380069915");

		accountOrderService.createOrder(account.getId(), o);

		o = new Order();
		o.setCarrier(Carrier.USPS);
		o.setId("9405903699300367183443");

		accountOrderService.createOrder(account.getId(), o);

		o = new Order();
		o.setCarrier(Carrier.USPS);
		o.setId("9405903699300368436531");

		accountOrderService.createOrder(account.getId(), o);

		o = new Order();
		o.setCarrier(Carrier.USPS);
		o.setId("9405903699300380069915");

		try {
			Order duplicateOrder = accountOrderService.createOrder(account.getId(), o);
			Assert.assertNull(duplicateOrder);
		} catch (DuplicateOrderException e) {
			logger.error(e.getMessage());
			Assert.assertEquals("PACK_ERR_0004: Package with id 9405903699300380069915 already exists in this account",
					e.getMessage());
		} finally {
			accountOrderService.deleteAccount(account.getId());
		}

	}
}
