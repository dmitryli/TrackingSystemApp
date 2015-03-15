package com.dimalimonov.tracking.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dimalimonov.tracking.domain.Account;
import com.dimalimonov.tracking.domain.Carrier;
import com.dimalimonov.tracking.domain.Order;
import com.dimalimonov.tracking.service.CarrierService;
import com.dimalimonov.tracking.service.OrderService;
import com.dimalimonov.tracking.service.ProfileService;
import com.dimalimonov.tracking.util.TrackingUtils;

@Service("orderService")
public class OrderServiceImpl implements OrderService {

	private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

	private final static String UPS_PREFIX = "1Z";

	@Value("${notification.wait.period}")
	private Integer notificationWaitPeriod = 0;

	@Autowired
	@Qualifier("upsService")
	private CarrierService upsService = null;

	@Autowired
	@Qualifier("uspsService")
	private CarrierService uspsService = null;

	@Autowired
	private ProfileService profileService = null;

	@Override
	public Order createOrder(Order order) {

		order.setCreationTime(System.currentTimeMillis());
		order.setLastNotificationTime(System.currentTimeMillis());

		if (order.getId().startsWith(UPS_PREFIX)) {
			order.setCarrier(Carrier.UPS);
			order.setTreashold(profileService.getUpsThreshold());
			order.setActivities(upsService.getActivityList(order.getId()));
		} else {
			order.setCarrier(Carrier.USPS);
			order.setTreashold(profileService.getUspsThreshold());
			order.setActivities(uspsService.getActivityList(order.getId()));
		}

		return order;
	}

	@Override
	public Order updateOrderInfo(Order order) {

		if (order.getStatus() == null || !order.getStatus().equals(OrderService.ORDER_DELIVERED)) {

			if (order.getId().startsWith(UPS_PREFIX)) {
				order.setActivities(upsService.getActivityList(order.getId()));
			} else {
				order.setActivities(uspsService.getActivityList(order.getId()));
			}
		}

		return order;
	}

	@Override
	public boolean shouldNotify(Account a, Order o) {
		boolean shouldNotify = false;
		long currentTime = System.currentTimeMillis();
		long lastNotificationTime = o.getLastNotificationTime();
		long creationTime = o.getCreationTime();

		if (!o.getStatus().equals(OrderService.ORDER_DELIVERED)) {
			Integer thresholdBarrier = null;

			if (o.getCarrier() == Carrier.UPS) {
				thresholdBarrier = a.getProfile().getThreashold().get(Carrier.UPS.toString());
			} else if (o.getCarrier() == Carrier.USPS) {
				thresholdBarrier = a.getProfile().getThreashold().get(Carrier.UPS.toString());
			} else {
				logger.warn("Carrier not supported");
				return false;
			}
			// first notification ever should be sent if not delivered and
			// threshold exceeded, and subsequent is sent if wait period has
			// passed
			if (currentTime - creationTime > TrackingUtils.timeInHoursToMillis(thresholdBarrier)
					&& currentTime - lastNotificationTime > TrackingUtils.timeInHoursToMillis(notificationWaitPeriod)
					&& !o.isSilenceNotifications()) {
				shouldNotify = true;
			}

		}
		return shouldNotify;
	}

	@Override
	public Order getOrderById(Account a, String orderId) {
		Order returnValue = null;
		List<Order> orders = a.getOrders();
		for (Order o : orders) {
			if (o.getId().equals(orderId)) {
				returnValue = o;
				break;
			}
		}
		return returnValue;
	}
}
