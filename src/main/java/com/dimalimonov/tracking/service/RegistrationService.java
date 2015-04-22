package com.dimalimonov.tracking.service;

import com.dimalimonov.tracking.domain.User;
import com.dimalimonov.tracking.domain.RegistrationResult;

public interface RegistrationService {

	public RegistrationResult register(User c);
}
