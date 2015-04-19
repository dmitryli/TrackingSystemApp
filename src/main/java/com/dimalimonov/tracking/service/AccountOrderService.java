package com.dimalimonov.tracking.service;

import java.util.List;

import com.dimalimonov.tracking.domain.Account;
import com.dimalimonov.tracking.domain.Order;

public interface AccountOrderService {

	public abstract Account createAccount(Account account);

	public abstract Account findAccount(String id);

	public abstract void deleteAccount(String id);

	public abstract List<Account> findAccounts();

	public abstract Order createOrder(String accountId, Order order);

	public abstract List<Order> createOrders(String accountId, List<Order> orders);

	public abstract Order findOrder(String accountId, String orderId);

	public abstract List<Order> findOrders(String accountId);

	public abstract Order archiveOrder(String accountId, Order order);

	public abstract Order activateOrder(String accountId, Order order);

	public abstract Order changeState(String accountId, Order order);

	public abstract Order muteNotifications(String accountId, Order order);

	public abstract Order updateTreshold(String accountId, Order order);
	
	public Order updateDescription(String accountId, Order order);
}