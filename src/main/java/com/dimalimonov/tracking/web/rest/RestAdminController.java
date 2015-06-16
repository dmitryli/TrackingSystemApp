package com.dimalimonov.tracking.web.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dimalimonov.tracking.domain.Account;
import com.dimalimonov.tracking.domain.Feedback;
import com.dimalimonov.tracking.domain.User;
import com.dimalimonov.tracking.service.AccountDeliveriesService;
import com.dimalimonov.tracking.service.FeedbackService;
import com.dimalimonov.tracking.service.UserService;

@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class RestAdminController {

	private static final Logger logger = LoggerFactory.getLogger(RestAdminController.class);

	@Autowired
	private FeedbackService feedbackService = null;

	@Autowired
	private AccountDeliveriesService accountDeliveriesSerice = null;

	@Autowired
	private UserService userService = null;

	@RequestMapping(value = "/accounts", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public List<Account> getAccounts() {
		logger.info("getallAccounts");
		return accountDeliveriesSerice.findAccounts();
	}

	@RequestMapping(value = "/accounts", method = RequestMethod.DELETE, params = "sure")
	public void deleteAccounts() {
		logger.info("deleteAllAccounts");
		List<Account> all = accountDeliveriesSerice.findAccounts();
		for (Account a : all) {
			accountDeliveriesSerice.deleteAccount(a.getId());
		}

	}

	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public List<User> findUsers() {
		return userService.find();

	}

	@RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE)
	public void findUserById(@PathVariable("id") String id) {
		userService.deleteById(id);

	}

	@RequestMapping(value = "/feedbacks", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public List<Feedback> geAllFeedbacks() {

		return feedbackService.findAll();
	}

	@RequestMapping(value = "/feedbacks/{id}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Feedback geFeedbacks(@PathVariable("id") String id) {

		return feedbackService.findById(id);
	}

	@RequestMapping(value = "/feedbacks", method = RequestMethod.DELETE)
	public void deleteAll() {
		List<Feedback> all = feedbackService.findAll();
		for (Feedback a : all) {
			feedbackService.delete(a.getId());
		}
	}
}
