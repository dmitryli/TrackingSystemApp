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
import com.dimalimonov.tracking.domain.Profile;
import com.dimalimonov.tracking.service.AccountService;
import com.dimalimonov.tracking.service.OrderService;

@RestController
public class ViewProfileController {

	private static final Logger logger = LoggerFactory.getLogger(ViewProfileController.class);

	@Autowired
	private AccountService accountService = null;

	@Autowired
	private OrderService orderService = null;

	@RequestMapping(value = "/ui/accounts/{id}/profile", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ModelAndView getAccountProfile(@PathVariable("id") String accountId) {

		logger.info("getAccount {}", accountId);
		Account account = accountService.find(accountId);
		Profile p = account.getProfile();
		ModelAndView mv = new ModelAndView("profile", "profile", p);
		return mv;
	}

	// @RequestMapping(value = "/accounts/{id}/profile", method =
	// RequestMethod.PUT, consumes = { MediaType.APPLICATION_JSON_VALUE },
	// produces = { MediaType.APPLICATION_JSON_VALUE })
	// public Profile addOrders(@PathVariable("id") String accountId,
	// @RequestBody Profile profile) {
	// logger.info("modify profile {}", accountId);
	// Account account = accountService.find(accountId);
	// account.setProfile(profile);
	// accountService.update(account);
	// return profile;
	//
	// }

}