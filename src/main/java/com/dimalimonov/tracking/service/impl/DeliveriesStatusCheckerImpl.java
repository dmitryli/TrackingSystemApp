package com.dimalimonov.tracking.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.dimalimonov.tracking.domain.Account;
import com.dimalimonov.tracking.domain.Carrier;
import com.dimalimonov.tracking.domain.Delivery;
import com.dimalimonov.tracking.domain.DeliveryState;
import com.dimalimonov.tracking.domain.DeliveryStatus;
import com.dimalimonov.tracking.errors.ErrorCodes;
import com.dimalimonov.tracking.errors.ErrorMessages;
import com.dimalimonov.tracking.service.AccountDeliveriesService;
import com.dimalimonov.tracking.service.CarrierService;
import com.dimalimonov.tracking.util.TrackingUtils;

@Service
public class DeliveriesStatusCheckerImpl {
	private static final Logger logger = LoggerFactory.getLogger(DeliveriesStatusCheckerImpl.class);

	//Every hour
	private static final long DELIVERIES_STATUS_CHECK_INTERVAL = 3600000;

	@Value("${scheduler.on}")
	private boolean schedulerOn = true;

	@Autowired
	@Qualifier("upsService")
	private CarrierService upsService = null;

	@Autowired
	@Qualifier("uspsService")
	private CarrierService uspsService = null;
	
	@Autowired
	@Qualifier("fedexService")
	private CarrierService fedexService = null;

	@Autowired
	private AccountDeliveriesService accountService = null;

	// Run every 60 minutes
	@Scheduled(fixedRate = DELIVERIES_STATUS_CHECK_INTERVAL)
	public void updateDeliveries() {
		logger.info("Starting scheduled account updater");
		List<Account> accounts = accountService.findAccounts();

		for (Account a : accounts) {
			List<Delivery> deliveries = a.getDeliveries();

			for (Delivery d : deliveries) {
				// test only in transit deliveries
				if (d== null || !d.getDeliveryStatus().equals(DeliveryStatus.DELIVERED)) {
					updateCarrierShippingInfo(d);
				}
			}
				
			accountService.update(a);
		}
		logger.info("Finished scheduled account updater");
	}

	private Delivery updateCarrierShippingInfo(Delivery delivery) {
		if (delivery.getCarrier().equals(Carrier.UPS)) {
			delivery.setActivities(upsService.getActivityList(delivery.getId()));
		} else if (delivery.getCarrier().equals(Carrier.USPS)) {
			delivery.setActivities(uspsService.getActivityList(delivery.getId()));
		}else if (delivery.getCarrier().equals(Carrier.FEDEX)) {
			delivery.setActivities(fedexService.getActivityList(delivery.getId()));
		}
		else {
			logger.error(ErrorCodes.CARRIER_NOT_SUPPORTED
					+ String.format(ErrorMessages.CARRIER_NOT_SUPPORTED_ERROR_MESSAGE.getErrorMessage(), delivery
							.getCarrier().toString()));
		}

		return delivery;
	}
}
