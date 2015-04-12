package com.dimalimonov.tracking.domain;

public class RegistrationResult {

	private Account account = null;
	private Customer customer = null;

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

}
