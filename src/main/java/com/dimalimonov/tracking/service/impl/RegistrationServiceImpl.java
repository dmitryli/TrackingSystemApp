package com.dimalimonov.tracking.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dimalimonov.tracking.domain.Account;
import com.dimalimonov.tracking.domain.RegistrationResult;
import com.dimalimonov.tracking.domain.User;
import com.dimalimonov.tracking.service.AccountDeliveriesService;
import com.dimalimonov.tracking.service.EmailService;
import com.dimalimonov.tracking.service.RegistrationService;
import com.dimalimonov.tracking.service.UserService;

@Service("registrationService")
public class RegistrationServiceImpl implements RegistrationService {

	private static final Logger logger = LoggerFactory.getLogger(RegistrationServiceImpl.class);

	@Autowired
	private AccountDeliveriesService accountService = null;

	@Autowired
	private UserService userService = null;

	@Autowired
	private EmailService emailService = null;

	@Override
	public RegistrationResult register(User c) {
		logger.info("Starting registration for user {}", c.getEmail());
		c = userService.create(c);

		Account account = new Account();
		account = accountService.createAccount(account);

		c.addAccountId(account.getId());

		userService.update(c);

		RegistrationResult registration = new RegistrationResult();
		registration.setAccount(account);
		registration.setUser(c);
		logger.info("Completed registration for user {} with account {}", c.getEmail(), c.getAccountId());

		emailService.sendWelcomeEmail(c.getEmail(), c.getDisplayName(), c.getAccountId());

		return registration;
	}

}
