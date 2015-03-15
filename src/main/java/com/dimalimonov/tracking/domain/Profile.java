package com.dimalimonov.tracking.domain;

import java.util.HashMap;
import java.util.Map;

public class Profile {
	private String userName = null;
	private String email = null;
	private String password = null;
	private boolean emailOnNewOrders = true;
	private boolean emailOnOrderStateChanges = true;
	private Map<String, Integer> threashold = new HashMap<String, Integer>();

	public Map<String, Integer> getThreashold() {
		return threashold;
	}

	public void addCarrierThreshold(Carrier carrierName, Integer hours) {
		threashold.put(carrierName.toString(), hours);
	}

	public boolean isEmailOnNewOrders() {
		return emailOnNewOrders;
	}

	public void setEmailOnNewOrders(boolean emailOnNewOrders) {
		this.emailOnNewOrders = emailOnNewOrders;
	}

	public boolean isEmailOnOrderStateChanges() {
		return emailOnOrderStateChanges;
	}

	public void setEmailOnOrderStateChanges(boolean emailOnOrderStateChanges) {
		this.emailOnOrderStateChanges = emailOnOrderStateChanges;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setThreashold(Map<String, Integer> threashold) {
		this.threashold = threashold;
	}

}
