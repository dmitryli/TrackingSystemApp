package com.dimalimonov.tracking.service.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.dimalimonov.tracking.domain.Account;
import com.dimalimonov.tracking.domain.Order;
import com.dimalimonov.tracking.service.AccountService;
import com.dimalimonov.tracking.service.EmailService;
import com.dimalimonov.tracking.service.OrderService;

@Service
public class SchedulingServiceImpl {
	private static final Logger logger = LoggerFactory.getLogger(SchedulingServiceImpl.class);

	private static final long ONE_HOUR_TIME_INTERVAL = 3600000;

	private static final long TEN_MIN_TIME_INTERVAL = 600000;

	private static final long TEST_SEC_TIME_INTERVAL = 30000;

	@Value("${scheduler.on}")
	private boolean schedulerOn = true;

	@Autowired
	private AccountService accountService = null;

	@Autowired
	private OrderService orderService = null;

	@Autowired
	private EmailService emailService = null;

	@Scheduled(fixedRate = TEN_MIN_TIME_INTERVAL)
	public void notifyAccounts() {
		if (schedulerOn) {
			logger.info("Starting account notifications", new Date(System.currentTimeMillis()));

			for (Account a : accountService.findAll()) {
				logger.info("Looking into account {}, {}", a.getId(), a.getProfile().getUserName());

				boolean updatedOrders = false;
				if (a.getOrders() != null && a.getOrders().size() > 0) {

					updatedOrders = isOrderUpdated(a);

					// save account if any order was updated
					if (updatedOrders) {
						accountService.update(a);

					}

				}

			}
		} else {
			logger.info("scheduler set to off");
		}

	}

	private boolean isOrderUpdated(Account a) {
		boolean updatedOrders = false;
		if (a.getOrders() != null && a.getOrders().size() > 0) {
			for (Order o : a.getOrders()) {
				logger.info("Looking into order {}", o.getId());
				Order updatedOrder = orderService.updateOrderInfo(o);

				if (!updatedOrder.equals(o)) {
					updatedOrders = true;
					emailService.sendNotificationEmail(a, o.getId(), o.getStatus());
				}
				// for each order, see if it is a time for notification
				else {
					if (orderService.shouldNotify(a, o)) {
						logger.info("Notification for account {} and order {} should be sent", a.getId(), o.getId());
						updatedOrders = true;
						o.setLastNotificationTime(System.currentTimeMillis());
						emailService.sendThresholdExceededEmail(a, o.getId(), o.getStatus());
					}
				}

			}
		}

		// save account - will store new status of orders or notification time
		accountService.update(a);

		return updatedOrders;
	}
}
