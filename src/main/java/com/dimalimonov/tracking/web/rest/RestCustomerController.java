package com.dimalimonov.tracking.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dimalimonov.tracking.domain.Customer;
import com.dimalimonov.tracking.domain.Link;
import com.dimalimonov.tracking.service.CustomerService;
import com.dimalimonov.tracking.util.Constants;

@RestController
public class RestCustomerController {

	@Autowired
	private CustomerService customerService = null;

	@RequestMapping(value = "/customers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, params = { "email" })
	public ResponseEntity<Link> findCustomerByEmail(@RequestParam("email") String email) {
		Link l = null;
		ResponseEntity<Link> re = null;

		Customer customer = customerService.findByEmail(email);
		if (customer != null) {
			l = new Link();
			l.setRel("customer");
			l.setHref(String.format(Constants.CUSTOMER_URI, customer.getId()));
			re = ResponseEntity.ok(l);
		} else {
			re = new ResponseEntity<Link>(HttpStatus.NOT_FOUND);
		}
		return re;

	}

	@RequestMapping(value = "/customers/{id}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Customer findCustomerById(@PathVariable("id") String id) {
		return customerService.findById(id);

	}

	@RequestMapping(value = "/customers/{id}", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Customer updateCustomerSettings(@PathVariable("id") String id, @RequestBody Customer customer) {
		Customer c = customerService.findById(id);
		// Id, Role and AccountId cannot be changed using REST API.
		if (c != null) {
			customer.setId(c.getId());
			customer.setRole(c.getRole());
			customer.addAccountId(c.getAccountId());

			c = customerService.update(customer);
		}
		return c;

	}

}
