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
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dimalimonov.tracking.domain.Order;
import com.dimalimonov.tracking.domain.OrderCollection;
import com.dimalimonov.tracking.errors.DuplicateOrderException;
import com.dimalimonov.tracking.errors.RestError;
import com.dimalimonov.tracking.service.AccountOrderService;
import com.dimalimonov.tracking.util.Constants;

@RestController
public class RestOrderController {

	private static final Logger logger = LoggerFactory.getLogger(RestOrderController.class);

	@Autowired
	private AccountOrderService accountService = null;

	@RequestMapping(value = "/accounts/{id}/orders", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<Order>> addOrders(@PathVariable("id") String accountId,
			@RequestBody OrderCollection orders) {
		logger.info("creating new orders for account {}", accountId);
		List<Order> list = accountService.createOrders(accountId, orders.getOrders());

		HttpHeaders headers = new HttpHeaders();
		String location = String.format(Constants.ACCOUNT_URI, accountId);
		headers.setLocation(URI.create(location));
		ResponseEntity<List<Order>> re = new ResponseEntity<List<Order>>(list, headers, HttpStatus.CREATED);

		return re;
	}

	@RequestMapping(value = "/accounts/{id}/orders", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public List<Order> getOrders(@PathVariable("id") String accountId) {
		logger.info("find orders for account {}", accountId);
		return accountService.findOrders(accountId);
	}

	@RequestMapping(value = "/accounts/{id}/orders/{orderId}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Order getOrderById(@PathVariable("id") String accountId, @PathVariable("orderId") String orderId) {
		logger.info("find order {} for account {}", orderId, accountId);
		return accountService.findOrder(accountId, orderId);
	}

	@RequestMapping(value = "/accounts/{id}/orders/{orderId}/mute", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public void muteOrder(@PathVariable("id") String accountId, @PathVariable("orderId") String orderId,
			@RequestBody Order order) {
		logger.info("mute order {} on account {}", orderId, accountId);
		order.setId(orderId);
		accountService.muteNotifications(accountId, order);

	}

	@RequestMapping(value = "/accounts/{id}/orders/{orderId}/threshold", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public void updateThreshold(@PathVariable("id") String accountId, @PathVariable("orderId") String orderId,
			@RequestBody Order order) {
		logger.info("changing threshold for order {} on account {}", orderId, accountId);
		order.setId(orderId);
		accountService.updateTreshold(accountId, order);

	}

	@RequestMapping(value = "/accounts/{id}/orders/{orderId}/state", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public void changeState(@PathVariable("id") String accountId, @PathVariable("orderId") String orderId,
			@RequestBody Order order) {
		logger.info("changing threshold for order {} on account {}", orderId, accountId);
		order.setId(orderId);
		accountService.changeState(accountId, order);

	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(DuplicateOrderException.class)
	public RestError handleDuplicateCustomer(DuplicateOrderException dup) {
		RestError re = new RestError();
		re.setCode(dup.getErrorCode());
		re.setMessage(dup.getMessage());
		return re;
	}
}
