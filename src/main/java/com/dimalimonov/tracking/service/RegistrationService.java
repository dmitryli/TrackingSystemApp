package com.dimalimonov.tracking.service;

import com.dimalimonov.tracking.domain.Customer;
import com.dimalimonov.tracking.domain.RegistrationResult;

public interface RegistrationService {

	public RegistrationResult register(Customer c);
}
