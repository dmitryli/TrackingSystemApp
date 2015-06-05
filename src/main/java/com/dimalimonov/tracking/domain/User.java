package com.dimalimonov.tracking.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "id", "displayName", "email", "password", "phoneNumber", "emailWhenNewOrderAdded",
		"emailWhenOrderStateChanges", "emailWhenThresholdExceeded", "emailNotificationTime", "defaultThreshold",
		"accountId", "accountLink" })
@JsonIgnoreProperties("role")
@JsonInclude(Include.NON_EMPTY)
public class User {
	private String id = null;
	private String displayName = null;
	private String email = null;
	private String phoneNumber = null;
	private String password = null;
	private String role = null;
	private boolean emailWhenNewOrderAdded = false;
	private boolean emailWhenOrderStateChanges = false;
	private boolean emailWhenThresholdExceeded = true;
	private int archiveWaitPeriod = 1;
	private int emailNotificationTime = 0;

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
		String account = null;
		if (accountsList != null && accountsList.size() > 0) {
			account = accountsList.get(0);
		}
		return account;
	}

	public int getEmailNotificationTime() {
		return emailNotificationTime;
	}

	public void setEmailNotificationTime(int emailNotificationTime) {
		this.emailNotificationTime = emailNotificationTime;
	}


	public int getArchiveWaitPeriod() {
		return archiveWaitPeriod;
	}

	public void setArchiveWaitPeriod(int archiveWaitPeriod) {
		this.archiveWaitPeriod = archiveWaitPeriod;
	}


}
