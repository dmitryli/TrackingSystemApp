package com.dimalimonov.tracking.service.impl;

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
import com.dimalimonov.tracking.domain.Delivery;
import com.dimalimonov.tracking.domain.DeliveryState;
import com.dimalimonov.tracking.errors.DuplicateDeliveryException;
import com.dimalimonov.tracking.errors.ErrorCodes;
import com.dimalimonov.tracking.errors.ErrorMessages;
import com.dimalimonov.tracking.service.AccountDeliveriesService;
import com.dimalimonov.tracking.service.CarrierService;

@Service("accountOrderService")
@Profile("production")
public class AccountDeliveriesServiceImpl implements AccountDeliveriesService {
	private static final Logger logger = LoggerFactory.getLogger(AccountDeliveriesServiceImpl.class);
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
	public List<Delivery> createDeliveries(String accountId, List<Delivery> deliveries) {
		Account account = findAccount(accountId);

		List<Delivery> existingOrders = account.getDeliveries();

		if (existingOrders != null && existingOrders.size() > 0 && deliveries != null && deliveries.size() > 0) {
			List<String> duplicateList = getDuplicateList(existingOrders, deliveries);
			if (duplicateList.size() > 0) {
				throw new DuplicateDeliveryException(duplicateList.get(0));
			}
		}

		if (deliveries != null) {
			for (Delivery o : deliveries) {
				o = generateDefaultOrderInformation(o);
				o = setCarrierStatusUpdates(o);
				logger.info("Adding delivery {} to account {}", o.getId(), account.getId());
				account.addDelivery(o);

			}
		}

		update(account);
		return account.getDeliveries();

	}

	@Override
	public Delivery createDelivery(String accountId, Delivery delivery) {
		return createDeliveries(accountId, Collections.singletonList(delivery)).get(0);
	}

	@Override
	public List<Delivery> findDeliveries(String accountId) {
		List<Delivery> deliveries = null;
		Account account = findAccount(accountId);
		if (account != null) {
			deliveries = account.getDeliveries();
		}
		return deliveries;

	}

	@Override
	public Delivery findDelivery(String accountId, String deliveryId) {
		return findOrder(findAccount(accountId), deliveryId);
	}

	private Delivery findOrder(Account account, String deliveryId) {
		Delivery delivery = null;
		if (account != null) {
			List<Delivery> deliveries = account.getDeliveries();
			if (deliveries != null) {
				for (Delivery o : deliveries) {
					if (o.getId().equals(deliveryId)) {
						delivery = o;
						break;
					}
				}
			}

		}
		return delivery;
	}

	@Override
	public Delivery archiveDelivery(String accountId, Delivery delivery) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Delivery activateDelivery(String accountId, Delivery delivery) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Delivery muteNotifications(String accountId, Delivery delivery) {
		Account a = findAccount(accountId);
		Delivery o = findOrder(a, delivery.getId());
		if (o != null) {
			o.setMuteNotifications(delivery.isMuteNotifications());
		}

		update(a);
		return o;
	}

	@Override
	public Delivery updateTreshold(String accountId, Delivery delivery) {
		Account a = findAccount(accountId);
		Delivery o = findOrder(a, delivery.getId());
		if (o != null) {
			o.setThreshold(delivery.getThreshold());
		}

		update(a);
		return o;
	}

	@Override
	public Delivery changeState(String accountId, Delivery delivery) {
		Account a = findAccount(accountId);
		Delivery o = findOrder(a, delivery.getId());
		if (o != null) {
			o.setState(delivery.getState());
		}

		update(a);
		return o;
	}

	@Override
	public Delivery updateDescription(String accountId, Delivery delivery) {
		Account a = findAccount(accountId);
		Delivery o = findOrder(a, delivery.getId());
		if (o != null) {
			o.setDescription(delivery.getDescription());
		}

		update(a);
		return o;
	}

	private List<String> getDuplicateList(List<Delivery> existingOrders, List<Delivery> deliveries) {
		List<String> duplicateList = new ArrayList<String>();

		for (Delivery o : existingOrders) {
			for (Delivery no : deliveries) {
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

	private Delivery generateDefaultOrderInformation(Delivery delivery) {

		delivery.setCreationTime(System.currentTimeMillis());
		delivery.setLastNotificationTime(System.currentTimeMillis());
		delivery.setMuteNotifications(false);
		delivery.setState(DeliveryState.ACTIVE);

		if ((delivery.getCarrier() != null && delivery.getCarrier().equals(Carrier.UPS))
				|| delivery.getId().startsWith(UPS_PREFIX)) {
			delivery.setCarrier(Carrier.UPS);
			delivery.setThreshold(upsThreshold);
		} else if ((delivery.getCarrier() != null && delivery.getCarrier().equals(Carrier.USPS))
				|| delivery.getId().startsWith(USPS_PREFIX)) {
			delivery.setCarrier(Carrier.USPS);
			delivery.setThreshold(uspsThreshold);
		} else {
			logger.error(ErrorCodes.CARRIER_NOT_SUPPORTED
					+ String.format(ErrorMessages.CARRIER_NOT_SUPPORTED_ERROR_MESSAGE.getErrorMessage(), delivery
							.getCarrier().toString()));
		}

		return delivery;
	}

	private Delivery setCarrierStatusUpdates(Delivery delivery) {
		if (delivery.getCarrier().equals(Carrier.UPS)) {
			delivery.setActivities(upsService.getActivityList(delivery.getId()));
		} else if (delivery.getCarrier().equals(Carrier.USPS)) {
			delivery.setActivities(uspsService.getActivityList(delivery.getId()));
		} else {
			logger.error(ErrorCodes.CARRIER_NOT_SUPPORTED
					+ String.format(ErrorMessages.CARRIER_NOT_SUPPORTED_ERROR_MESSAGE.getErrorMessage(), delivery
							.getCarrier().toString()));
		}

		return delivery;

	}

}
