package com.dimalimonov.tracking.ut.account;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

import com.dimalimonov.tracking.domain.Account;
import com.dimalimonov.tracking.domain.Carrier;
import com.dimalimonov.tracking.domain.Order;
import com.dimalimonov.tracking.domain.OrderDeliveryStatus;
import com.dimalimonov.tracking.domain.OrderState;
import com.dimalimonov.tracking.errors.DuplicateOrderException;
import com.dimalimonov.tracking.errors.ErrorCodes;
import com.dimalimonov.tracking.errors.ErrorMessages;
import com.dimalimonov.tracking.service.AccountOrderService;
import com.dimalimonov.tracking.service.CarrierService;

@Service("accountOrderService")
@Profile("test")
public class TestAccountOrdersServiceImpl implements AccountOrderService {
	private static final Logger logger = LoggerFactory.getLogger(TestAccountOrdersServiceImpl.class);
	private static final String COLLECTION_NAME = "ptrackaccounts";

	private final static String UPS_PREFIX = "1Z";
	private final static String USPS_PREFIX = "9405";

	@Value("${notification.wait.period}")
	private Integer notificationWaitPeriod = 0;

	@Value("${usps.threshold}")
	public Integer uspsThreshold = null;

	@Value("${ups.threshold}")
	public Integer upsThreshold = null;

	@Autowired
	@Qualifier("upsService")
	private CarrierService upsService = null;

	@Autowired
	@Qualifier("uspsService")
	private CarrierService uspsService = null;

	@Autowired
	private MongoOperations mongoOperations = null;

	@Override
	public Account createAccount(Account account) {

		logger.info("Attepting create an account");
		mongoOperations.insert(account, COLLECTION_NAME);
		logger.info("new id is {}", account.getId());
		return account;
	}

	@Override
	public List<Account> findAccounts() {
		List<Account> users = mongoOperations.findAll(Account.class, COLLECTION_NAME);
		return users;
	}

	@Override
	public Account findAccount(String id) {
		Account account = mongoOperations.findById(id, Account.class, COLLECTION_NAME);
		return account;

	}

	@Override
	public void deleteAccount(String id) {
		logger.info("Attepting delete of account {}", id);
		Account account = mongoOperations.findById(id, Account.class, COLLECTION_NAME);
		mongoOperations.remove(account, COLLECTION_NAME);
		logger.info("Deleted account {}", id);

	}

	@Override
	public List<Order> createOrders(String accountId, List<Order> orders) {
		Account account = findAccount(accountId);

		List<Order> existingOrders = account.getOrders();

		if (existingOrders != null && existingOrders.size() > 0 && orders != null && orders.size() > 0) {
			List<String> duplicateList = getDuplicateList(existingOrders, orders);
			if (duplicateList.size() > 0) {
				throw new DuplicateOrderException(duplicateList.get(0));
			}
		}

		if (orders != null) {
			for (Order o : orders) {
				o = generateDefaultOrderInformation(o);
				logger.info("Adding order {} to account {}", o.getId(), account.getId());
				account.addOrder(o);

			}
		}

		update(account);
		return account.getOrders();

	}

	@Override
	public Order createOrder(String accountId, Order order) {
		return createOrders(accountId, Collections.singletonList(order)).get(0);
	}

	@Override
	public List<Order> findOrders(String accountId) {
		List<Order> orders = null;
		Account account = findAccount(accountId);
		if (account != null) {
			orders = account.getOrders();
		}
		return orders;

	}

	@Override
	public Order findOrder(String accountId, String orderId) {
		return findOrder(findAccount(accountId), orderId);
	}

	private Order findOrder(Account account, String orderId) {
		Order order = null;
		if (account != null) {
			List<Order> orders = account.getOrders();
			if (orders != null) {
				for (Order o : orders) {
					if (o.getId().equals(orderId)) {
						order = o;
						break;
					}
				}
			}

		}
		return order;
	}

	@Override
	public Order archiveOrder(String accountId, Order order) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Order activateOrder(String accountId, Order order) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Order muteNotifications(String accountId, Order order) {
		Account a = findAccount(accountId);
		Order o = findOrder(a, order.getId());
		if (o != null) {
			o.setMuteNotifications(order.isMuteNotifications());
		}

		update(a);
		return o;
	}

	@Override
	public Order updateTreshold(String accountId, Order order) {
		Account a = findAccount(accountId);
		Order o = findOrder(a, order.getId());
		if (o != null) {
			o.setThreshold(order.getThreshold());
		}

		update(a);
		return o;
	}

	@Override
	public Order changeState(String accountId, Order order) {
		Account a = findAccount(accountId);
		Order o = findOrder(a, order.getId());
		if (o != null) {
			o.setState(order.getState());
		}

		update(a);
		return o;
	}

	@Override
	public Order updateDescription(String accountId, Order order) {
		Account a = findAccount(accountId);
		Order o = findOrder(a, order.getId());
		if (o != null) {
			o.setDescription(order.getDescription());
		}

		update(a);
		return o;
	}

	private List<String> getDuplicateList(List<Order> existingOrders, List<Order> orders) {
		List<String> duplicateList = new ArrayList<String>();

		for (Order o : existingOrders) {
			for (Order no : orders) {
				if (o.getId().equals(no.getId())) {
					duplicateList.add(o.getId());
				}
			}
		}

		return duplicateList;
	}

	private void update(Account account) {
		mongoOperations.save(account, COLLECTION_NAME);
	}

	private Order generateDefaultOrderInformation(Order order) {

		order.setCreationTime(System.currentTimeMillis());
		order.setLastNotificationTime(System.currentTimeMillis());
		order.setMuteNotifications(false);
		order.setState(OrderState.ACTIVE);
		order.setOrderDeliveryStatus(OrderDeliveryStatus.PRESHIPPED);

		if ((order.getCarrier() != null && order.getCarrier().equals(Carrier.UPS))
				|| order.getId().startsWith(UPS_PREFIX)) {
			order.setCarrier(Carrier.UPS);
			order.setThreshold(upsThreshold);
		} else if ((order.getCarrier() != null && order.getCarrier().equals(Carrier.USPS))
				|| order.getId().startsWith(USPS_PREFIX)) {
			order.setCarrier(Carrier.USPS);
			order.setThreshold(uspsThreshold);
		} else {
			logger.error(ErrorCodes.CARRIER_NOT_SUPPORTED
					+ String.format(ErrorMessages.CARRIER_NOT_SUPPORTED_ERROR_MESSAGE.getErrorMessage(), order
							.getCarrier().toString()));
		}

		return order;
	}

}
