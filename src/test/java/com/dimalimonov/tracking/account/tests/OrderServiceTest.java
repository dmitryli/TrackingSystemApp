package com.dimalimonov.tracking.account.tests;

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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.dimalimonov.tracking.TrackingSystemApplication;
import com.dimalimonov.tracking.domain.Account;
import com.dimalimonov.tracking.domain.Carrier;
import com.dimalimonov.tracking.domain.Order;
import com.dimalimonov.tracking.domain.OrderState;
import com.dimalimonov.tracking.domain.OrderStatus;
import com.dimalimonov.tracking.service.AccountOrderService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TrackingSystemApplication.class)
@WebAppConfiguration
public class OrderServiceTest {

	private static final Logger logger = LoggerFactory.getLogger(OrderServiceTest.class);
	@Value("${usps.threshold}")
	public Integer uspsThreshold = null;

	@Value("${ups.threshold}")
	public Integer upsThreshold = null;

	@Autowired
	private AccountOrderService accountOrderService = null;

	@Test
	public void createOneOrder() {
		Account account = new Account();
		account = accountOrderService.createAccount(account);
		Assert.assertNotNull(account.getId());

		Order o = new Order();
		o.setCarrier(Carrier.USPS);
		o.setId("9405903699300380069915");

		accountOrderService.createOrder(account.getId(), o);

		Account find = accountOrderService.findAccount(account.getId());
		Assert.assertNotNull(find);
		Assert.assertNotNull(find.getOrders());
		Assert.assertTrue(find.getOrders().size() == 1);

		Order result = find.getOrders().get(0);
		Assert.assertTrue(result.getId().equals("9405903699300380069915"));
		Assert.assertTrue(result.getCarrier().equals(Carrier.USPS));
		Assert.assertEquals(result.getThreshold(), uspsThreshold.intValue());
		Assert.assertEquals(result.getState(), OrderState.ACTIVE);
		Assert.assertEquals(result.getShippingStatus(), OrderStatus.PRESHIPPED);

		accountOrderService.deleteAccount(account.getId());

	}

	@Test
	public void archiveOrder() {
		Account account = new Account();
		account = accountOrderService.createAccount(account);
		Assert.assertNotNull(account.getId());

		Order o = new Order();
		o.setCarrier(Carrier.USPS);
		o.setId("9405903699300380069915");

		accountOrderService.createOrder(account.getId(), o);

		Order find = accountOrderService.findOrder(account.getId(), "9405903699300380069915");
		Assert.assertNotNull(find);

		find.setState(OrderState.ARCHIVED);
		accountOrderService.changeState(account.getId(), find);

		Order find2 = accountOrderService.findOrder(account.getId(), "9405903699300380069915");
		Assert.assertEquals(OrderState.ARCHIVED, find2.getState());

		accountOrderService.deleteAccount(account.getId());

	}

	@Test
	public void activateOrder() {
		Account account = new Account();
		account = accountOrderService.createAccount(account);
		Assert.assertNotNull(account.getId());

		Order o = new Order();
		o.setCarrier(Carrier.USPS);
		o.setId("9405903699300380069915");

		accountOrderService.createOrder(account.getId(), o);

		Order find = accountOrderService.findOrder(account.getId(), "9405903699300380069915");
		Assert.assertNotNull(find);

		// set to archive
		find.setState(OrderState.ARCHIVED);
		accountOrderService.changeState(account.getId(), find);

		Order find2 = accountOrderService.findOrder(account.getId(), "9405903699300380069915");
		Assert.assertEquals(OrderState.ARCHIVED, find2.getState());

		// set to active
		find2.setState(OrderState.ACTIVE);
		accountOrderService.changeState(account.getId(), find2);

		Order find3 = accountOrderService.findOrder(account.getId(), "9405903699300380069915");

		Assert.assertEquals(OrderState.ACTIVE, find3.getState());

		accountOrderService.deleteAccount(account.getId());

	}

	@Test
	public void updateOrderThreshold() {
		Account account = new Account();
		account = accountOrderService.createAccount(account);
		Assert.assertNotNull(account.getId());

		Order o = new Order();
		o.setCarrier(Carrier.USPS);
		o.setId("9405903699300380069915");

		accountOrderService.createOrder(account.getId(), o);

		Order find = accountOrderService.findOrder(account.getId(), "9405903699300380069915");
		Assert.assertNotNull(find);

		find.setThreshold(1000);
		accountOrderService.updateTreshold(account.getId(), find);

		Order find2 = accountOrderService.findOrder(account.getId(), "9405903699300380069915");
		Assert.assertEquals(1000, find2.getThreshold());

		accountOrderService.deleteAccount(account.getId());

	}

	@Test
	public void muteOrder() {
		Account account = new Account();
		account = accountOrderService.createAccount(account);
		Assert.assertNotNull(account.getId());

		Order o = new Order();
		o.setCarrier(Carrier.USPS);
		o.setId("9405903699300380069915");

		accountOrderService.createOrder(account.getId(), o);

		Order find = accountOrderService.findOrder(account.getId(), "9405903699300380069915");
		Assert.assertNotNull(find);

		find.setMuteNotifications(true);
		accountOrderService.muteNotifications(account.getId(), find);

		Order find2 = accountOrderService.findOrder(account.getId(), "9405903699300380069915");
		Assert.assertEquals(true, find2.isMuteNotifications());

		accountOrderService.deleteAccount(account.getId());

	}

	@Test
	public void muteUnmuteOrder() {
		Account account = new Account();
		account = accountOrderService.createAccount(account);
		Assert.assertNotNull(account.getId());

		Order o = new Order();
		o.setCarrier(Carrier.USPS);
		o.setId("9405903699300380069915");

		accountOrderService.createOrder(account.getId(), o);

		Order find = accountOrderService.findOrder(account.getId(), "9405903699300380069915");
		Assert.assertNotNull(find);

		// mute
		find.setMuteNotifications(true);
		accountOrderService.muteNotifications(account.getId(), find);

		Order find2 = accountOrderService.findOrder(account.getId(), "9405903699300380069915");
		Assert.assertEquals(true, find2.isMuteNotifications());

		// unmute
		find.setMuteNotifications(false);
		accountOrderService.muteNotifications(account.getId(), find);
		Order find3 = accountOrderService.findOrder(account.getId(), "9405903699300380069915");
		Assert.assertEquals(false, find3.isMuteNotifications());

		accountOrderService.deleteAccount(account.getId());

	}

	@Test
	public void findOrder() {
		Account account = new Account();
		account = accountOrderService.createAccount(account);
		Assert.assertNotNull(account.getId());

		Order o = new Order();
		o.setCarrier(Carrier.USPS);
		o.setId("9405903699300380069915");

		accountOrderService.createOrder(account.getId(), o);

		Order result = accountOrderService.findOrder(account.getId(), "9405903699300380069915");
		Assert.assertTrue(result.getId().equals("9405903699300380069915"));
		Assert.assertTrue(result.getCarrier().equals(Carrier.USPS));
		Assert.assertEquals(result.getThreshold(), uspsThreshold.intValue());
		Assert.assertEquals(result.getState(), OrderState.ACTIVE);
		Assert.assertEquals(result.getShippingStatus(), OrderStatus.PRESHIPPED);

		accountOrderService.deleteAccount(account.getId());

	}

	@Test
	public void createMultipleOrders() {
		Account account = new Account();
		account = accountOrderService.createAccount(account);
		Assert.assertNotNull(account.getId());

		List<Order> list = new LinkedList<Order>();
		Order o = new Order();
		o.setCarrier(Carrier.USPS);
		o.setId("9405903699300380069915");
		list.add(o);

		o = new Order();
		o.setCarrier(Carrier.USPS);
		o.setId("9405903699300372857186");
		list.add(o);

		accountOrderService.createOrders(account.getId(), list);

		Account find = accountOrderService.findAccount(account.getId());
		Assert.assertNotNull(find);
		Assert.assertNotNull(find.getOrders());
		Assert.assertTrue(find.getOrders().size() == 2);

		Assert.assertTrue(find.getOrders().get(0).getId().equals("9405903699300380069915"));
		Assert.assertTrue(find.getOrders().get(0).getCarrier().equals(Carrier.USPS));
		Assert.assertEquals(find.getOrders().get(0).getThreshold(), uspsThreshold.intValue());

		Assert.assertTrue(find.getOrders().get(1).getId().equals("9405903699300372857186"));
		Assert.assertTrue(find.getOrders().get(1).getCarrier().equals(Carrier.USPS));
		Assert.assertEquals(find.getOrders().get(1).getThreshold(), uspsThreshold.intValue());

		accountOrderService.deleteAccount(account.getId());

	}

	@Test
	public void findOrders() {
		Account account = new Account();
		account = accountOrderService.createAccount(account);
		Assert.assertNotNull(account.getId());

		List<Order> list = new LinkedList<Order>();
		Order o = new Order();
		o.setCarrier(Carrier.USPS);
		o.setId("9405903699300380069915");
		list.add(o);

		o = new Order();
		o.setCarrier(Carrier.USPS);
		o.setId("9405903699300372857186");
		list.add(o);

		accountOrderService.createOrders(account.getId(), list);

		List<Order> orders = accountOrderService.findOrders(account.getId());
		Assert.assertNotNull(orders);
		Assert.assertTrue(orders.size() == 2);

		Assert.assertTrue(orders.get(0).getId().equals("9405903699300380069915"));
		Assert.assertTrue(orders.get(0).getCarrier().equals(Carrier.USPS));
		Assert.assertEquals(orders.get(0).getThreshold(), uspsThreshold.intValue());

		Assert.assertTrue(orders.get(1).getId().equals("9405903699300372857186"));
		Assert.assertTrue(orders.get(1).getCarrier().equals(Carrier.USPS));
		Assert.assertEquals(orders.get(1).getThreshold(), uspsThreshold.intValue());

		accountOrderService.deleteAccount(account.getId());

	}
}
