package com.dimalimonov.tracking.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dimalimonov.tracking.domain.Customer;
import com.dimalimonov.tracking.service.CustomerService;

@RestController
public class RestCustomerController {

	@Autowired
	private CustomerService customerService = null;

	@RequestMapping(value = "/customers/{id}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Customer findCustomerById(@PathVariable("id") String id) {
		return customerService.findById(id);

	}

	@RequestMapping(value = "/customers", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE }, params = { "email" })
	public Customer findCustomerByEmail(@RequestParam("email") String email) {
		return customerService.findByEmail(email);

	}

}
