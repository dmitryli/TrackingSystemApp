package com.dimalimonov.tracking.web.ui;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.dimalimonov.tracking.domain.Account;
import com.dimalimonov.tracking.domain.Order;
import com.dimalimonov.tracking.service.AccountService;
import com.dimalimonov.tracking.service.OrderService;

@Controller
public class ViewOrderController {

	private static final Logger logger = LoggerFactory.getLogger(ViewOrderController.class);

	@Autowired
	private AccountService accountService = null;

	@Autowired
	private OrderService orderService = null;

	/*
	 * @RequestMapping(value = "/accounts/{id}/orders", method =
	 * RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE },
	 * produces = { MediaType.APPLICATION_JSON_VALUE }) public List<Order>
	 * addOrders(@PathVariable("id") String accountId, @RequestBody
	 * OrderCollection orders) { logger.info("addOrder {}", accountId);
	 * List<Order> list = accountService.addOrders(accountId, orders); return
	 * list;
	 * 
	 * }
	 */

	@RequestMapping(value = "/ui/accounts/{id}/orders", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ModelAndView getOrders(@PathVariable("id") String accountId) {
		logger.info("getOrders {}", accountId);
		Account account = accountService.find(accountId);

		List<Order> orders = account.getOrders();
		ModelAndView mv = new ModelAndView("orders", "ordersList", orders);
		return mv;
	}
}
