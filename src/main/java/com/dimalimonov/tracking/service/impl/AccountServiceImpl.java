package com.dimalimonov.tracking.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

import com.dimalimonov.tracking.domain.Account;
import com.dimalimonov.tracking.domain.Order;
import com.dimalimonov.tracking.domain.OrderCollection;
import com.dimalimonov.tracking.service.AccountService;
import com.dimalimonov.tracking.service.EmailService;
import com.dimalimonov.tracking.service.OrderService;
import com.dimalimonov.tracking.service.ProfileService;

@Service("accountService")
public class AccountServiceImpl implements AccountService {
	private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);
	private static final String COLLECTION_NAME = "trackingaccounts";

	@Autowired
	private MongoOperations mongoOperations = null;

	@Autowired
	private ProfileService profileService = null;

	@Autowired
	private OrderService orderService = null;

	@Autowired
	private EmailService emailService = null;

	@Override
	public Account create(Account account) {

		account.setProfile(profileService.addDefaultSettings(account.getProfile()));
		mongoOperations.insert(account, COLLECTION_NAME);
		logger.info("new id is {}", account.getId());
		try {
			emailService.sendWeclomeEmail(account);
		} catch (Exception e) {
			logger.error("Cannot send email", e);
		}

		return account;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dimalimonov.tracking.service.AccountService#find()
	 */

	@Override
	public List<Account> findAll() {
		List<Account> users = mongoOperations.findAll(Account.class, COLLECTION_NAME);
		return users;
	}

	@Override
	public Account find(String id) {
		Account account = mongoOperations.findById(id, Account.class, COLLECTION_NAME);

		if (account != null && account.getOrders() != null && account.getOrders().size() > 0) {
			for (Order o : account.getOrders()) {
				orderService.updateOrderInfo(o);
			}
			update(account);
		}
		return account;

	}

	@Override
	public void update(Account account) {
		mongoOperations.save(account, COLLECTION_NAME);
	}

	@Override
	public List<Order> addOrders(String accountId, OrderCollection orders) {
		Account account = find(accountId);

		List<Order> list = null;

		if (account != null) {
			for (Order o : orders.getOrders()) {
				orderService.createOrder(o);
				account.addOrder(o);

			}
			update(account);

			for (Order o : orders.getOrders()) {
				try {
					emailService.sendNewOrderEmail(account, o.getId(), o.getStatus());
				} catch (Exception e) {
					logger.error("Cannot send email", e);
				}

			}
			list = account.getOrders();
		}

		return list;
	}

	@Override
	public void delete(String id) {
		Account account = mongoOperations.findById(id, Account.class, COLLECTION_NAME);
		mongoOperations.remove(account, COLLECTION_NAME);
		try {
			emailService.sendGoodByeEmail(account);
		} catch (Exception e) {
			logger.error("Cannot send email", e);
		}

	}

}
