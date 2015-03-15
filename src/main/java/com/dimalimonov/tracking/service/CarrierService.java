package com.dimalimonov.tracking.service;

import java.util.List;

import com.dimalimonov.tracking.domain.Activity;

public interface CarrierService {

	public abstract List<Activity> getActivityList(String id);

}