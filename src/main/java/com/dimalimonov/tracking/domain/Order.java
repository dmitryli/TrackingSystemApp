package com.dimalimonov.tracking.domain;

import java.util.List;

public class Order {

	private String id = null;
	private Carrier carrier = null;
	private String description = null;
	private long creationTime = 0;
	private long lastNotificationTime = 0;
	private int threshold = 0;
	private boolean muteNotifications = false;
	private long whenNotificationShouldBeSent = 0;
	private OrderState state = null;
//	private OrderStatus shippingStatus = null;
	private OrderDeliveryStatus orderDeliveryStatus = null;
	private List<Activity> activities = null;

	public String getDateTime() {
		String dateTime = null;
		if (getActivities() != null) {
			dateTime = getActivities().get(0).getDate() + " : " + getActivities().get(0).getTime();
		}
		return dateTime;
	}

	public boolean isMuteNotifications() {
		return muteNotifications;
	}

	public void setMuteNotifications(boolean muteNotifications) {
		this.muteNotifications = muteNotifications;
	}

	public long getWhenNotificationShouldBeSent() {
		return whenNotificationShouldBeSent;
	}

	public void setWhenNotificationShouldBeSent(long whenNotificationShouldBeSent) {
		this.whenNotificationShouldBeSent = whenNotificationShouldBeSent;
	}

	public OrderState getState() {
		return state;
	}

	public void setState(OrderState state) {
		this.state = state;
	}


	public OrderDeliveryStatus getOrderDeliveryStatus() {
		return orderDeliveryStatus;
	}

	public void setOrderDeliveryStatus(OrderDeliveryStatus orderDeliveryStatus) {
		this.orderDeliveryStatus = orderDeliveryStatus;
	}

	public List<Activity> getActivities() {
		return activities;
	}

	public void setActivities(List<Activity> activities) {
		this.activities = activities;
		if (this.activities!=null && this.activities.size() > 0) {
			Activity lastActivity = activities.get(0);
			if (lastActivity.getStatusDescription().contains("was delivered")) {
				setOrderDeliveryStatus(OrderDeliveryStatus.DELIVERED);
			} else if (lastActivity.getStatusDescription().contains("Out for Delivery") || lastActivity.getStatusDescription().contains("Sorting") || 
					lastActivity.getStatusDescription().contains("Accepted") || lastActivity.getStatusDescription().contains("Arrived") || lastActivity.getStatusDescription().contains("Departed")) {
				setOrderDeliveryStatus(OrderDeliveryStatus.SHIPPED);
			}  else if (lastActivity.getStatusDescription().contains("Pre-Shipment")){
				setOrderDeliveryStatus(OrderDeliveryStatus.PRESHIPPED);
			}  else if (lastActivity.getStatusDescription().contains("Duplicate")){
				setOrderDeliveryStatus(OrderDeliveryStatus.DUPLICATE);
			}
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Carrier getCarrier() {
		return carrier;
	}

	public void setCarrier(Carrier carrier) {
		this.carrier = carrier;
	}

	public long getLastNotificationTime() {
		return lastNotificationTime;
	}

	public void setLastNotificationTime(long lastNotificationTime) {
		this.lastNotificationTime = lastNotificationTime;
	}

	public long getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}

	public int getThreshold() {
		return threshold;
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	

}
