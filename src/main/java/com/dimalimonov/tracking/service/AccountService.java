package com.dimalimonov.tracking.service;

import java.util.List;

import com.dimalimonov.tracking.domain.Account;
import com.dimalimonov.tracking.domain.Order;
import com.dimalimonov.tracking.domain.OrderCollection;

public interface AccountService {

	public abstract Account create(Account account);

	public abstract Account find(String id);

	public abstract void delete(String id);

	public abstract void update(Account account);

	public abstract List<Account> findAll();

	public abstract List<Order> addOrders(String accountId, OrderCollection orders);

}