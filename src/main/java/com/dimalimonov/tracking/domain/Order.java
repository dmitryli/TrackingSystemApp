package com.dimalimonov.tracking.domain;

import java.util.List;

public class Order {

	private String id = null;
	private Carrier carrier = null;
	private long creationTime = 0;
	private long lastNotificationTime = 0;
	private int threshold = 0;
	private boolean muteNotifications = false;
	private long whenNotificationShouldBeSent = 0;
	private OrderState state = null;
	private OrderStatus shippingStatus = null;

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

	public OrderStatus getShippingStatus() {
		return shippingStatus;
	}

	public void setShippingStatus(OrderStatus status) {
		this.shippingStatus = status;
	}

	public List<Activity> getActivities() {
		return activities;
	}

	public void setActivities(List<Activity> activities) {
		this.activities = activities;
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

}
