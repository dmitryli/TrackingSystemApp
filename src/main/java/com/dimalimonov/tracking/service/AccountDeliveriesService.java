package com.dimalimonov.tracking.service;

import java.util.List;

import com.dimalimonov.tracking.domain.Account;
import com.dimalimonov.tracking.domain.Delivery;

public interface AccountDeliveriesService {

	public abstract Account createAccount(Account account);

	public abstract Account findAccount(String id);

	public abstract void deleteAccount(String id);

	public abstract List<Account> findAccounts();

	public abstract Delivery createDelivery(String accountId, Delivery delivery);

	public abstract List<Delivery> createDeliveries(String accountId, List<Delivery> deliveries);

	public abstract Delivery findDelivery(String accountId, String deliveryId);

	public abstract List<Delivery> findDeliveries(String accountId);

	public abstract Delivery archiveDelivery(String accountId, Delivery delivery);

	public abstract Delivery activateDelivery(String accountId, Delivery delivery);

	public abstract Delivery changeState(String accountId, Delivery delivery);

	public abstract Delivery muteNotifications(String accountId, Delivery delivery);

	public abstract Delivery updateTreshold(String accountId, Delivery delivery);
	
	public Delivery updateDescription(String accountId, Delivery delivery);
}