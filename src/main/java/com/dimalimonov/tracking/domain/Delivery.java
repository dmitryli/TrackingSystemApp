package com.dimalimonov.tracking.domain;

import java.util.List;

public class Delivery {

	private String id = null;
	private Carrier carrier = null;
	private String description = null;
	private long creationTime = 0;
	private long lastNotificationTime = 0;
	private int threshold = 0;
	private boolean muteNotifications = false;
	private long whenNotificationShouldBeSent = 0;
	private DeliveryState state = null;
	private boolean isOverDue = false;
	private DeliveryStatus deliveryStatus = null;
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

	public DeliveryState getState() {
		return state;
	}

	public void setState(DeliveryState state) {
		this.state = state;
	}


	public DeliveryStatus getDeliveryStatus() {
		return deliveryStatus;
	}

	public void setDeliveryStatus(DeliveryStatus deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}

	public List<Activity> getActivities() {
		return activities;
	}

	public void setActivities(List<Activity> activities) {
		this.activities = activities;
		if (this.activities!=null && this.activities.size() > 0) {
			Activity lastActivity = activities.get(0);
			if (lastActivity.getStatusDescription().contains("was delivered")) {
				setDeliveryStatus(DeliveryStatus.DELIVERED);
			} else {
				setDeliveryStatus(DeliveryStatus.INTRANSIT);
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

	public boolean isOverDue() {
		return isOverDue;
	}

	public void setOverDue(boolean isOverDue) {
		this.isOverDue = isOverDue;
	}
	

}
