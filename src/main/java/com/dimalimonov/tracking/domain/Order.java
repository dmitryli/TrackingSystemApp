package com.dimalimonov.tracking.domain;

import java.util.List;

public class Order {

	private String id = null;
	private Carrier carrier = null;
	private long creationTime = 0;
	private boolean silenceNotifications = false;
	private long lastNotificationTime = 0;
	private int treashold = 0;

	public String getDateTime() {
		String dateTime = null;
		if (getActivities() != null) {
			dateTime = getActivities().get(0).getDate() + " : " + getActivities().get(0).getTime();
		}
		return dateTime;
	}

	public String getStatus() {
		String status = null;
		if (getActivities() != null) {
			status = getActivities().get(0).getStatusDescription();
		}
		return status;
	}

	private List<Activity> activities = null;

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

	public boolean isSilenceNotifications() {
		return silenceNotifications;
	}

	public void setSilenceNotifications(boolean silenceNotifications) {
		this.silenceNotifications = silenceNotifications;
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

	public int getTreashold() {
		return treashold;
	}

	public void setTreashold(int treashold) {
		this.treashold = treashold;
	}

}
