package com.dimalimonov.tracking.domain;

import java.util.ArrayList;
import java.util.List;

public class Account {

	private String id = null;
	private List<Order> orders = new ArrayList<Order>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void addOrder(Order o) {
		orders.add(o);
	}

}
