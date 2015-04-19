package com.dimalimonov.tracking.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dimalimonov.tracking.domain.Account;
import com.dimalimonov.tracking.domain.Customer;
import com.dimalimonov.tracking.domain.RegistrationResult;
import com.dimalimonov.tracking.service.AccountOrderService;
import com.dimalimonov.tracking.service.CustomerService;
import com.dimalimonov.tracking.service.EmailService;
import com.dimalimonov.tracking.service.RegistrationService;

@Service("registrationService")
public class RegistrationServiceImpl implements RegistrationService {

	private static final Logger logger = LoggerFactory.getLogger(RegistrationServiceImpl.class);

	@Autowired
	private AccountOrderService accountService = null;

	@Autowired
	private CustomerService customerService = null;
	
	@Autowired
	private EmailService emailService = null;

	@Override
	public RegistrationResult register(Customer c) {
		logger.info("Starting registration for customer {}", c.getEmail());
		c = customerService.create(c);

		Account account = new Account();
		account = accountService.createAccount(account);

		c.addAccountId(account.getId());

		customerService.update(c);

		RegistrationResult registration = new RegistrationResult();
		registration.setAccount(account);
		registration.setCustomer(c);
		logger.info("Completed registration for customer {} with account {}", c.getEmail(), c.getAccountId());
		
		emailService.sendWelcomeEmail(c.getEmail(), c.getDisplayName(), account.getId());
		return registration;
	}

}
