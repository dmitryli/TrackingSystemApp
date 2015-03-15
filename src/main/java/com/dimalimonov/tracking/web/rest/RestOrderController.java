package com.dimalimonov.tracking.web.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dimalimonov.tracking.domain.Account;
import com.dimalimonov.tracking.domain.Order;
import com.dimalimonov.tracking.domain.OrderCollection;
import com.dimalimonov.tracking.service.AccountService;
import com.dimalimonov.tracking.service.OrderService;

@RestController
public class RestOrderController {

	private static final Logger logger = LoggerFactory.getLogger(RestOrderController.class);

	@Autowired
	private AccountService accountService = null;

	@Autowired
	private OrderService orderService = null;

	@RequestMapping(value = "/accounts/{id}/orders", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public List<Order> addOrders(@PathVariable("id") String accountId, @RequestBody OrderCollection orders) {
		logger.info("addOrders {}", accountId);
		List<Order> list = accountService.addOrders(accountId, orders);
		return list;

	}

	@RequestMapping(value = "/accounts/{id}/orders", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public List<Order> getOrders(@PathVariable("id") String accountId) {
		logger.info("getOrders {}", accountId);
		Account account = accountService.find(accountId);
		return account.getOrders();
	}

	@RequestMapping(value = "/accounts/{id}/orders/{orderId}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Order getOrderById(@PathVariable("id") String accountId, @PathVariable("orderId") String orderId) {
		logger.info("getOrderById {}", accountId);
		Account account = accountService.find(accountId);

		return orderService.getOrderById(account, orderId);
	}

	@RequestMapping(value = "/accounts/{id}/orders/{orderId}", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Order updateOrderByID(@PathVariable("id") String accountId, @PathVariable("orderId") String orderId, @RequestBody Order order) {
		logger.info("updateOrderByID {}", accountId);
		Account account = accountService.find(accountId);
		Order orderById = orderService.getOrderById(account, orderId);
		orderById.setSilenceNotifications(order.isSilenceNotifications());
		orderById.setTreashold(order.getTreashold());
		accountService.update(account);
		return orderById;

	}
}
