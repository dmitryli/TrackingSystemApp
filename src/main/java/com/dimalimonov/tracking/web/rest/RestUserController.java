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

import com.dimalimonov.tracking.domain.User;
import com.dimalimonov.tracking.domain.Link;
import com.dimalimonov.tracking.service.UserService;
import com.dimalimonov.tracking.util.Constants;

@RestController
public class RestUserController {

	@Autowired
	private UserService userService = null;

	@RequestMapping(value = "/users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, params = { "email" })
	public ResponseEntity<Link> findCustomerByEmail(@RequestParam("email") String email) {
		Link l = null;
		ResponseEntity<Link> re = null;

		User user = userService.findByEmail(email);
		if (user != null) {
			l = new Link();
			l.setRel("user");
			l.setHref(String.format(Constants.USER_URI, user.getId()));
			re = ResponseEntity.ok(l);
		} else {
			re = new ResponseEntity<Link>(HttpStatus.NOT_FOUND);
		}
		return re;

	}

	@RequestMapping(value = "/users/{id}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public User findCustomerById(@PathVariable("id") String id) {
		return userService.findById(id);

	}

	@RequestMapping(value = "/users/{id}", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE })
	public User updateCustomerSettings(@PathVariable("id") String id, @RequestBody User user) {
		User c = userService.findById(id);
		// Id, Role and AccountId cannot be changed using REST API.
		if (c != null) {
			user.setId(c.getId());
			user.setRole(c.getRole());
			user.addAccountId(c.getAccountId());

			c = userService.update(user);
		}
		return c;

	}

}
