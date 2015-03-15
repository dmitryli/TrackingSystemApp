package com.dimalimonov.tracking.web.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.dimalimonov.tracking.domain.Account;
import com.dimalimonov.tracking.service.AccountService;
import com.dimalimonov.tracking.service.OrderService;

@RestController
public class ViewAccountController {

	private static final Logger logger = LoggerFactory.getLogger(ViewAccountController.class);

	@Autowired
	private AccountService accountService = null;

	@Autowired
	private OrderService orderService = null;

	// @RequestMapping(value = "/accounts", method = RequestMethod.GET, produces
	// = { MediaType.APPLICATION_JSON_VALUE })
	// public List<Account> getAccounts() {
	// logger.info("getallAccounts");
	// return accountService.findAll();
	// }
	//
	// @RequestMapping(value = "/accounts", method = RequestMethod.POST,
	// consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = {
	// MediaType.APPLICATION_JSON_VALUE })
	// public ResponseEntity<Account> createAccount(@RequestBody Account
	// account) {
	//
	// logger.info("Creating user account");
	// Account created = accountService.create(account);
	//
	// HttpHeaders headers = new HttpHeaders();
	// String location = String.format(Constants.ACCOUNT_URI, account.getId());
	// headers.setLocation(URI.create(location));
	// ResponseEntity<Account> re = new ResponseEntity<Account>(created,
	// headers, HttpStatus.CREATED);
	//
	// return re;
	// }
	//
	// @RequestMapping(value = "/accounts/{id}", method = RequestMethod.DELETE)
	// public void deleteAccount(@PathVariable("id") String id) {
	// accountService.delete(id);
	// }

	@RequestMapping(value = "/ui/accounts/{id}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ModelAndView getAccount(@PathVariable("id") String accountId) {
		logger.info("getAccount {}", accountId);
		Account c = accountService.find(accountId);

		ModelAndView mv = new ModelAndView("home", "account", c);
		return mv;

	}

}
