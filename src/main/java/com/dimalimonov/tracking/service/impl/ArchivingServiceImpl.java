package com.dimalimonov.tracking.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.dimalimonov.tracking.domain.Account;
import com.dimalimonov.tracking.domain.Delivery;
import com.dimalimonov.tracking.domain.DeliveryState;
import com.dimalimonov.tracking.domain.DeliveryStatus;
import com.dimalimonov.tracking.service.AccountDeliveriesService;
import com.dimalimonov.tracking.util.TrackingUtils;

@Service
public class ArchivingServiceImpl {
	private static final Logger logger = LoggerFactory.getLogger(ArchivingServiceImpl.class);

	// Every hour
	private static final long ARCHIVE_JOB_RUN_INTERVAL = 3600000;

	@Value("${scheduler.on}")
	private boolean schedulerOn = true;

	@Autowired
	private AccountDeliveriesService accountService = null;
	
	@Scheduled(fixedRate = ARCHIVE_JOB_RUN_INTERVAL)
	public void archiveDeliveries() {
		logger.info("Starting scheduled account deliveries archiver");
		List<Account> accounts = accountService.findAccounts();

		for (Account a : accounts) {
			List<Delivery> deliveries = a.getDeliveries();

			// TODO: we should really archive an hour after it was delivered
			for (Delivery d : deliveries) {
				if (d.getDeliveryStatus() != null) {
					if (d.getDeliveryStatus().equals(DeliveryStatus.DELIVERED) && d.getState().equals(DeliveryState.ACTIVE)) {
						if (System.currentTimeMillis() - d.getCreationTime() > getArchiveInterval()) {
							logger.info("archiving delivery {} on account {}", d.getId(), a.getId());
							d.setState(DeliveryState.ARCHIVED);
							accountService.changeState(a.getId(), d);
						}
					}
				}
				
			}
		}
		logger.info("Finished scheduled account deliveries archiver");
	}

	// If more than an hour passed from the delivery creation and it is delivered - archive it
	private long getArchiveInterval() {
		return TrackingUtils.timeInHoursToMillis(1);
	}

	// @Scheduled(fixedRate = TEST_SEC_TIME_INTERVAL)
	// public void notifyAccounts() {
	// if (schedulerOn) {
	// logger.info("Starting account notifications", new
	// Date(System.currentTimeMillis()));
	//
	// for (Account a : accountService.findAll()) {
	// // logger.info("Looking into account {}, {}", a.getId(),
	// a.getProfile().getUserName());
	//
	// boolean updatedOrders = false;
	// if (a.getOrders() != null && a.getOrders().size() > 0) {
	//
	// updatedOrders = isOrderUpdated(a);
	//
	// // save account if any order was updated
	// if (updatedOrders) {
	// // accountService.update(a);
	//
	// }
	//
	// }
	//
	// }
	// } else {
	// logger.info("scheduler set to off");
	// }
	//
	// }

	// private boolean isOrderUpdated(Account a) {
	// boolean updatedOrders = false;
	// if (a.getOrders() != null && a.getOrders().size() > 0) {
	// for (Order o : a.getOrders()) {
	// logger.info("Looking into order {}", o.getId());
	// Order updatedOrder = orderService.updateOrderInfo(o);
	//
	// if (!updatedOrder.equals(o)) {
	// updatedOrders = true;
	// emailService.sendNotificationEmail(a, o.getId(), o.getStatus());
	// }
	// // for each order, see if it is a time for notification
	// else {
	// if (orderService.shouldNotify(a, o)) {
	// logger.info("Notification for account {} and order {} should be sent",
	// a.getId(), o.getId());
	// updatedOrders = true;
	// o.setLastNotificationTime(System.currentTimeMillis());
	// emailService.sendThresholdExceededEmail(a, o.getId(), o.getStatus());
	// }
	// }
	//
	// }
	// }
	//
	// // save account - will store new status of orders or notification time
	// // accountService.update(a);
	//
	// return updatedOrders;
	// }
}
