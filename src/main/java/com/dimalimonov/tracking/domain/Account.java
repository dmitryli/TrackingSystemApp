package com.dimalimonov.tracking.domain;

import java.util.ArrayList;
import java.util.List;

public class Account {

	private String id = null;
	private List<Delivery> deliveries = new ArrayList<Delivery>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void addDelivery(Delivery o) {
		deliveries.add(o);
	}

	public List<Delivery> getDeliveries() {
		return deliveries;
	}
		
}
