package com.dimalimonov.tracking.service;

import com.dimalimonov.tracking.domain.Customer;

public interface CustomerService {

	public abstract Customer create(Customer customer);

	public abstract Customer findById(String id);

	public abstract Customer findByEmail(String id);

	public abstract void deleteById(String id);

	public abstract void deleteByEmail(String id);

	public abstract void update(Customer customer);

}