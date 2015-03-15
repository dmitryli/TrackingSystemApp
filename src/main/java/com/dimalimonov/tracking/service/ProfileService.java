package com.dimalimonov.tracking.service;

import com.dimalimonov.tracking.domain.Profile;

public interface ProfileService {

	public abstract Profile addDefaultSettings(Profile p);

	public abstract Integer getUspsThreshold();

	public abstract Integer getUpsThreshold();
}
