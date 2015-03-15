package com.dimalimonov.tracking.service;

import com.dimalimonov.tracking.domain.Account;
import com.dimalimonov.tracking.domain.Order;

public interface OrderService {

	public final String ORDER_DELIVERED = "DELIVERED";

	public abstract Order updateOrderInfo(Order order);

	public abstract Order createOrder(Order order);

	public abstract boolean shouldNotify(Account a, Order o);

	public abstract Order getOrderById(Account a, String orderId);
}
