package com.dimalimonov.tracking.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Customer {
	private String id = null;
	private String displayName = null;
	private String email = null;
	private String phoneNumber = null;
	private String password = null;
	private String role = null;
	private boolean emailWhenNewOrderAdded = true;
	private boolean emailWhenOrderStateChanges = true;
	private boolean emailWhenThresholdExceeded = true;

	private List<String> accountsList = new ArrayList<String>();
	private Map<String, Integer> defaultThreshold = new HashMap<String, Integer>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public boolean isEmailWhenNewOrderAdded() {
		return emailWhenNewOrderAdded;
	}

	public void setEmailWhenNewOrderAdded(boolean emailWhenNewOrderAdded) {
		this.emailWhenNewOrderAdded = emailWhenNewOrderAdded;
	}

	public boolean isEmailWhenOrderStateChanges() {
		return emailWhenOrderStateChanges;
	}

	public void setEmailWhenOrderStateChanges(boolean emailWhenOrderStateChanges) {
		this.emailWhenOrderStateChanges = emailWhenOrderStateChanges;
	}

	public boolean isEmailWhenThresholdExceeded() {
		return emailWhenThresholdExceeded;
	}

	public void setEmailWhenThresholdExceeded(boolean emailWhenThresholdExceeded) {
		this.emailWhenThresholdExceeded = emailWhenThresholdExceeded;
	}

	public void addCarrierThreshold(Carrier carrierName, Integer hours) {
		defaultThreshold.put(carrierName.toString(), hours);
	}

	public Map<String, Integer> getDefaultThreshold() {
		return defaultThreshold;
	}

	public void addAccountId(String accountId) {
		accountsList.add(accountId);
	}

	public String getAccountId() {
		return accountsList.get(0);
	}

}
