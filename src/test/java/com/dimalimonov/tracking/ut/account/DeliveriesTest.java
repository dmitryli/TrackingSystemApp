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
public class DeliveriesTest {

	private static final Logger logger = LoggerFactory.getLogger(DeliveriesTest.class);
	@Value("${usps.threshold}")
	public Integer uspsThreshold = null;

	@Value("${ups.threshold}")
	public Integer upsThreshold = null;

	@Autowired
	private AccountDeliveriesService accountOrderService = null;

	@Test
	public void createOneDelivery() {
		Account account = new Account();
		account = accountOrderService.createAccount(account);
		Assert.assertNotNull(account.getId());

		Delivery o = new Delivery();
		o.setCarrier(Carrier.USPS);
		o.setId("9405903699300380069915");
		o.setDescription("My favorite Order");


		accountOrderService.createDelivery(account.getId(), o);

		Account find = accountOrderService.findAccount(account.getId());
		Assert.assertNotNull(find);
		Assert.assertNotNull(find.getDeliveries());
		Assert.assertTrue(find.getDeliveries().size() == 1);

		Delivery result = find.getDeliveries().get(0);
		Assert.assertTrue(result.getId().equals("9405903699300380069915"));
		Assert.assertTrue(result.getCarrier().equals(Carrier.USPS));
		Assert.assertEquals(result.getDescription(),"My favorite Order");
		Assert.assertEquals(result.getThreshold(), uspsThreshold.intValue());
		Assert.assertEquals(result.getState(), DeliveryState.ACTIVE);

		accountOrderService.deleteAccount(account.getId());

	}

	@Test
	public void archiveDelivery() {
		Account account = new Account();
		account = accountOrderService.createAccount(account);
		Assert.assertNotNull(account.getId());

		Delivery o = new Delivery();
		o.setCarrier(Carrier.USPS);
		o.setId("9405903699300380069915");

		accountOrderService.createDelivery(account.getId(), o);

		Delivery find = accountOrderService.findDelivery(account.getId(), "9405903699300380069915");
		Assert.assertNotNull(find);

		find.setState(DeliveryState.ARCHIVED);
		accountOrderService.changeState(account.getId(), find);

		Delivery find2 = accountOrderService.findDelivery(account.getId(), "9405903699300380069915");
		Assert.assertEquals(DeliveryState.ARCHIVED, find2.getState());

		accountOrderService.deleteAccount(account.getId());

	}

	@Test
	public void activateDelivery() {
		Account account = new Account();
		account = accountOrderService.createAccount(account);
		Assert.assertNotNull(account.getId());

		Delivery o = new Delivery();
		o.setCarrier(Carrier.USPS);
		o.setId("9405903699300380069915");

		accountOrderService.createDelivery(account.getId(), o);

		Delivery find = accountOrderService.findDelivery(account.getId(), "9405903699300380069915");
		Assert.assertNotNull(find);

		// set to archive
		find.setState(DeliveryState.ARCHIVED);
		accountOrderService.changeState(account.getId(), find);

		Delivery find2 = accountOrderService.findDelivery(account.getId(), "9405903699300380069915");
		Assert.assertEquals(DeliveryState.ARCHIVED, find2.getState());

		// set to active
		find2.setState(DeliveryState.ACTIVE);
		accountOrderService.changeState(account.getId(), find2);

		Delivery find3 = accountOrderService.findDelivery(account.getId(), "9405903699300380069915");

		Assert.assertEquals(DeliveryState.ACTIVE, find3.getState());

		accountOrderService.deleteAccount(account.getId());

	}

	@Test
	public void updateDeliveryThreshold() {
		Account account = new Account();
		account = accountOrderService.createAccount(account);
		Assert.assertNotNull(account.getId());

		Delivery o = new Delivery();
		o.setCarrier(Carrier.USPS);
		o.setId("9405903699300380069915");

		accountOrderService.createDelivery(account.getId(), o);

		Delivery find = accountOrderService.findDelivery(account.getId(), "9405903699300380069915");
		Assert.assertNotNull(find);

		find.setThreshold(1000);
		accountOrderService.updateTreshold(account.getId(), find);

		Delivery find2 = accountOrderService.findDelivery(account.getId(), "9405903699300380069915");
		Assert.assertEquals(1000, find2.getThreshold());

		accountOrderService.deleteAccount(account.getId());

	}

	@Test
	public void muteDelivery() {
		Account account = new Account();
		account = accountOrderService.createAccount(account);
		Assert.assertNotNull(account.getId());

		Delivery o = new Delivery();
		o.setCarrier(Carrier.USPS);
		o.setId("9405903699300380069915");

		accountOrderService.createDelivery(account.getId(), o);

		Delivery find = accountOrderService.findDelivery(account.getId(), "9405903699300380069915");
		Assert.assertNotNull(find);

		find.setMuteNotifications(true);
		accountOrderService.muteNotifications(account.getId(), find);

		Delivery find2 = accountOrderService.findDelivery(account.getId(), "9405903699300380069915");
		Assert.assertEquals(true, find2.isMuteNotifications());

		accountOrderService.deleteAccount(account.getId());

	}

	@Test
	public void muteUnmuteDelivery() {
		Account account = new Account();
		account = accountOrderService.createAccount(account);
		Assert.assertNotNull(account.getId());

		Delivery o = new Delivery();
		o.setCarrier(Carrier.USPS);
		o.setId("9405903699300380069915");

		accountOrderService.createDelivery(account.getId(), o);

		Delivery find = accountOrderService.findDelivery(account.getId(), "9405903699300380069915");
		Assert.assertNotNull(find);

		// mute
		find.setMuteNotifications(true);
		accountOrderService.muteNotifications(account.getId(), find);

		Delivery find2 = accountOrderService.findDelivery(account.getId(), "9405903699300380069915");
		Assert.assertEquals(true, find2.isMuteNotifications());

		// unmute
		find.setMuteNotifications(false);
		accountOrderService.muteNotifications(account.getId(), find);
		Delivery find3 = accountOrderService.findDelivery(account.getId(), "9405903699300380069915");
		Assert.assertEquals(false, find3.isMuteNotifications());

		accountOrderService.deleteAccount(account.getId());

	}

	@Test
	public void findDelivery() {
		Account account = new Account();
		account = accountOrderService.createAccount(account);
		Assert.assertNotNull(account.getId());

		Delivery o = new Delivery();
		o.setCarrier(Carrier.USPS);
		o.setId("9405903699300380069915");

		accountOrderService.createDelivery(account.getId(), o);

		Delivery result = accountOrderService.findDelivery(account.getId(), "9405903699300380069915");
		Assert.assertTrue(result.getId().equals("9405903699300380069915"));
		Assert.assertTrue(result.getCarrier().equals(Carrier.USPS));
		Assert.assertEquals(result.getThreshold(), uspsThreshold.intValue());
		Assert.assertEquals(result.getState(), DeliveryState.ACTIVE);

		accountOrderService.deleteAccount(account.getId());

	}

	@Test
	public void createMultipleDeliveries() {
		Account account = new Account();
		account = accountOrderService.createAccount(account);
		Assert.assertNotNull(account.getId());

		List<Delivery> list = new LinkedList<Delivery>();
		Delivery o = new Delivery();
		o.setCarrier(Carrier.USPS);
		o.setId("9405903699300380069915");
		list.add(o);

		o = new Delivery();
		o.setCarrier(Carrier.USPS);
		o.setId("9405903699300372857186");
		list.add(o);

		accountOrderService.createDeliveries(account.getId(), list);

		Account find = accountOrderService.findAccount(account.getId());
		Assert.assertNotNull(find);
		Assert.assertNotNull(find.getDeliveries());
		Assert.assertTrue(find.getDeliveries().size() == 2);

		Assert.assertTrue(find.getDeliveries().get(0).getId().equals("9405903699300380069915"));
		Assert.assertTrue(find.getDeliveries().get(0).getCarrier().equals(Carrier.USPS));
		Assert.assertEquals(find.getDeliveries().get(0).getThreshold(), uspsThreshold.intValue());

		Assert.assertTrue(find.getDeliveries().get(1).getId().equals("9405903699300372857186"));
		Assert.assertTrue(find.getDeliveries().get(1).getCarrier().equals(Carrier.USPS));
		Assert.assertEquals(find.getDeliveries().get(1).getThreshold(), uspsThreshold.intValue());

		accountOrderService.deleteAccount(account.getId());

	}

	@Test
	public void findDeliveries() {
		Account account = new Account();
		account = accountOrderService.createAccount(account);
		Assert.assertNotNull(account.getId());

		List<Delivery> list = new LinkedList<Delivery>();
		Delivery o = new Delivery();
		o.setCarrier(Carrier.USPS);
		o.setId("9405903699300380069915");
		list.add(o);

		o = new Delivery();
		o.setCarrier(Carrier.USPS);
		o.setId("9405903699300372857186");
		list.add(o);

		accountOrderService.createDeliveries(account.getId(), list);

		List<Delivery> deliveries = accountOrderService.findDeliveries(account.getId());
		Assert.assertNotNull(deliveries);
		Assert.assertTrue(deliveries.size() == 2);

		Assert.assertTrue(deliveries.get(0).getId().equals("9405903699300380069915"));
		Assert.assertTrue(deliveries.get(0).getCarrier().equals(Carrier.USPS));
		Assert.assertEquals(deliveries.get(0).getThreshold(), uspsThreshold.intValue());

		Assert.assertTrue(deliveries.get(1).getId().equals("9405903699300372857186"));
		Assert.assertTrue(deliveries.get(1).getCarrier().equals(Carrier.USPS));
		Assert.assertEquals(deliveries.get(1).getThreshold(), uspsThreshold.intValue());

		accountOrderService.deleteAccount(account.getId());

	}
	
	@Test
	public void findActiveDeliveries() {
		Account account = new Account();
		account = accountOrderService.createAccount(account);
		Assert.assertNotNull(account.getId());

		List<Delivery> list = new LinkedList<Delivery>();
		Delivery o = new Delivery();
		o.setCarrier(Carrier.USPS);
		o.setId("9405903699300380069915");
		list.add(o);

		o = new Delivery();
		o.setCarrier(Carrier.USPS);
		o.setId("9405903699300372857186");
		list.add(o);

		accountOrderService.createDeliveries(account.getId(), list);
		
		o.setState(DeliveryState.ARCHIVED);
		accountOrderService.changeState(account.getId(), o);

		List<Delivery> deliveries = accountOrderService.findDeliveriesByState(account.getId(), DeliveryState.ACTIVE);
		Assert.assertNotNull(deliveries);
		Assert.assertTrue(deliveries.size() == 1 );
		
		List<Delivery> deliveries2 = accountOrderService.findDeliveriesByState(account.getId(), DeliveryState.ARCHIVED);
		Assert.assertNotNull(deliveries2);
		Assert.assertTrue(deliveries2.size() == 1 );


		accountOrderService.deleteAccount(account.getId());

	}
}
