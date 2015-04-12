package com.dimalimonov.tracking.web.rest;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dimalimonov.tracking.domain.Account;
import com.dimalimonov.tracking.service.AccountOrderService;
import com.dimalimonov.tracking.util.Constants;

@RestController
public class RestAccountController {

	private static final Logger logger = LoggerFactory.getLogger(RestAccountController.class);

	@Autowired
	private AccountOrderService accountService = null;

	@RequestMapping(value = "/accounts", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Account> createAccount(@RequestBody Account account) {

		logger.info("Creating user account");
		Account created = accountService.createAccount(account);

		HttpHeaders headers = new HttpHeaders();
		String location = String.format(Constants.ACCOUNT_URI, account.getId());
		headers.setLocation(URI.create(location));
		ResponseEntity<Account> re = new ResponseEntity<Account>(created, headers, HttpStatus.CREATED);

		return re;
	}

	@RequestMapping(value = "/accounts/{id}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Account> getAccount(@PathVariable("id") String accountId) {
		logger.info("getAccount {}", accountId);
		Account c = accountService.findAccount(accountId);

		ResponseEntity<Account> re = null;
		if (c != null) {
			re = new ResponseEntity<Account>(c, HttpStatus.OK);
		} else {
			re = new ResponseEntity<Account>(c, HttpStatus.NOT_FOUND);
		}

		return re;
	}

	@RequestMapping(value = "/accounts/{id}", method = RequestMethod.DELETE)
	public void deleteAccount(@PathVariable("id") String id) {
		accountService.deleteAccount(id);
	}

}
