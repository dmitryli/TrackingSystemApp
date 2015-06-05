package com.dimalimonov.tracking.web.rest;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dimalimonov.tracking.domain.User;
import com.dimalimonov.tracking.domain.Link;
import com.dimalimonov.tracking.domain.RegistrationResult;
import com.dimalimonov.tracking.errors.DuplicateUserException;
import com.dimalimonov.tracking.errors.RestError;
import com.dimalimonov.tracking.service.RegistrationService;
import com.dimalimonov.tracking.util.PTrackIUrlService;
import com.dimalimonov.tracking.util.PTrackIUrlServiceImpl;

@RestController
public class RestRegistrationController {

	@Autowired
	private PTrackIUrlService pTrackIUrlService = null;
	
	@Autowired
	private RegistrationService registrationService = null;

	@RequestMapping(value = "/registration", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<Link>> registerNewCustomer(@RequestBody User user) {

		RegistrationResult register = registrationService.register(user);

		String accountLocation =  pTrackIUrlService.getSingleAccountsURI( register.getAccount().getId()); 
				
		String userLocation =pTrackIUrlService.getSingleUsersURI(user.getId());

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(URI.create(accountLocation));

		Link accountLink = new Link("account", accountLocation);
		Link userLink = new Link("user", userLocation);
		List<Link> list = new LinkedList<Link>();
		list.add(accountLink);
		list.add(userLink);

		ResponseEntity<List<Link>> re = new ResponseEntity<List<Link>>(list, headers, HttpStatus.CREATED);

		return re;
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(DuplicateUserException.class)
	public RestError handleDuplicateCustomer(DuplicateUserException dup) {
		RestError re = new RestError();
		re.setCode(dup.getErrorCode());
		re.setMessage(dup.getMessage());
		return re;
	}

}
