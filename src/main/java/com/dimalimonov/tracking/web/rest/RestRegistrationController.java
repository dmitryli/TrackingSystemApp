package com.dimalimonov.tracking.web.rest;

import java.net.URI;

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

import com.dimalimonov.tracking.domain.Account;
import com.dimalimonov.tracking.domain.Customer;
import com.dimalimonov.tracking.domain.RegistrationResult;
import com.dimalimonov.tracking.errors.DuplicateCustomerException;
import com.dimalimonov.tracking.errors.RestError;
import com.dimalimonov.tracking.service.RegistrationService;
import com.dimalimonov.tracking.util.Constants;

@RestController
public class RestRegistrationController {

	@Autowired
	private RegistrationService registrationService = null;

	@RequestMapping(value = "/registration", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Account> registerNewCustomer(@RequestBody Customer customer) {

		RegistrationResult register = registrationService.register(customer);
		HttpHeaders headers = new HttpHeaders();
		String location = String.format(Constants.ACCOUNT_URI, register.getAccount().getId());
		headers.setLocation(URI.create(location));
		ResponseEntity<Account> re = new ResponseEntity<Account>(register.getAccount(), headers, HttpStatus.CREATED);

		return re;
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(DuplicateCustomerException.class)
	public RestError handleDuplicateCustomer(DuplicateCustomerException dup) {
		RestError re = new RestError();
		re.setCode(dup.getErrorCode());
		re.setMessage(dup.getMessage());
		return re;
	}

}
