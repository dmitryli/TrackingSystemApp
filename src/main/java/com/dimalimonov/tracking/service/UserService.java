package com.dimalimonov.tracking.service;

import java.util.List;

import com.dimalimonov.tracking.domain.User;

public interface UserService {

	public abstract User create(User user);
	
	public abstract List<User> find();

	public abstract User findById(String id);

	public abstract User findByEmail(String id);

	public abstract void deleteById(String id);

	public abstract void deleteByEmail(String id);

	public abstract User update(User user);

}