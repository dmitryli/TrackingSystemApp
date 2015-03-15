package com.dimalimonov.tracking.web.rest;

import java.net.URI;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dimalimonov.tracking.domain.Account;
import com.dimalimonov.tracking.service.AccountService;
import com.dimalimonov.tracking.service.OrderService;
import com.dimalimonov.tracking.util.Constants;

@RestController
public class RestAccountController {

	private static final Logger logger = LoggerFactory.getLogger(RestAccountController.class);

	@Autowired
	private AccountService accountService = null;

	@Autowired
	private OrderService orderService = null;

	@RequestMapping(value = "/accounts", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public List<Account> getAccounts() {

		logger.info("getallAccounts");
		return accountService.findAll();
	}

	@RequestMapping(value = "/accounts", method = RequestMethod.DELETE, params = "sure")
	public void deleteAccounts() {
		logger.info("deleteAllAccounts");
		List<Account> all = accountService.findAll();
		for (Account a : all) {
			accountService.delete(a.getId());
		}

	}

	@RequestMapping(value = "/accounts", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Account> createAccount(@RequestBody Account account) {

		logger.info("Creating user account");
		Account created = accountService.create(account);

		HttpHeaders headers = new HttpHeaders();
		String location = String.format(Constants.ACCOUNT_URI, account.getId());
		headers.setLocation(URI.create(location));
		ResponseEntity<Account> re = new ResponseEntity<Account>(created, headers, HttpStatus.CREATED);

		return re;
	}

	@RequestMapping(value = "/accounts/{id}", method = RequestMethod.DELETE)
	public void deleteAccount(@PathVariable("id") String id) {
		accountService.delete(id);
	}

	@RequestMapping(value = "/accounts/{id}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Account> getAccount(@PathVariable("id") String accountId) {
		logger.info("getAccount {}", accountId);
		Account c = accountService.find(accountId);

		ResponseEntity<Account> re = null;
		if (c != null) {
			re = new ResponseEntity<Account>(c, HttpStatus.OK);
		} else {
			re = new ResponseEntity<Account>(c, HttpStatus.NOT_FOUND);
		}

		return re;
	}

}
