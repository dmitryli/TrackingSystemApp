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

import com.dimalimonov.tracking.domain.Delivery;
import com.dimalimonov.tracking.domain.DeliveriesCollection;
import com.dimalimonov.tracking.domain.DeliveryState;
import com.dimalimonov.tracking.errors.DuplicateDeliveryException;
import com.dimalimonov.tracking.errors.RestError;
import com.dimalimonov.tracking.service.AccountDeliveriesService;
import com.dimalimonov.tracking.util.Constants;

@RestController
public class RestDeliveryController {

	private static final Logger logger = LoggerFactory.getLogger(RestDeliveryController.class);

	@Autowired
	private AccountDeliveriesService accountService = null;

	@RequestMapping(value = "/accounts/{id}/deliveries", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<Delivery>> addDeliveries(@PathVariable("id") String accountId,
			@RequestBody DeliveriesCollection deliveriesCollection) {
		logger.info("creating new deliveries for account {}", accountId);
		List<Delivery> list = accountService.createDeliveries(accountId, deliveriesCollection.getDeliveries());

		HttpHeaders headers = new HttpHeaders();
		String location = String.format(Constants.ACCOUNT_URI, accountId);
		headers.setLocation(URI.create(location));
		ResponseEntity<List<Delivery>> re = new ResponseEntity<List<Delivery>>(list, headers, HttpStatus.CREATED);
		return re;
	}

	@RequestMapping(value = "/accounts/{id}/deliveries", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public List<Delivery> getDeliveries(@PathVariable("id") String accountId) {
		logger.info("find deliveries for account {}", accountId);
		return accountService.findDeliveries(accountId);
	}
	
	
	@RequestMapping(value = "/accounts/{id}/deliveries", method = RequestMethod.GET,params="active", produces = { MediaType.APPLICATION_JSON_VALUE })
	public List<Delivery> getActiveDeliveries(@PathVariable("id") String accountId) {
		logger.info("find active deliveries for account {}", accountId);
		return accountService.findDeliveriesByState(accountId, DeliveryState.ACTIVE);
	}


	@RequestMapping(value = "/accounts/{id}/deliveries", method = RequestMethod.GET,params="archived", produces = { MediaType.APPLICATION_JSON_VALUE })
	public List<Delivery> getArchivedDeliveries(@PathVariable("id") String accountId) {
		logger.info("find archived deliveries for account {}", accountId);
		return accountService.findDeliveriesByState(accountId, DeliveryState.ARCHIVED);
	}

	
	@RequestMapping(value = "/accounts/{id}/deliveries/{deliveryId}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Delivery getDeliveryById(@PathVariable("id") String accountId, @PathVariable("deliveryId") String deliveryId) {
		logger.info("find delivery {} for account {}", deliveryId, accountId);
		return accountService.findDelivery(accountId, deliveryId);
	}

	@RequestMapping(value = "/accounts/{id}/deliveries/{deliveryId}/mute", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public void muteDelivery(@PathVariable("id") String accountId, @PathVariable("deliveryId") String deliveryId,
			@RequestBody Delivery delivery) {
		logger.info("mute delivery {} on account {}", deliveryId, accountId);
		delivery.setId(deliveryId);
		accountService.muteNotifications(accountId, delivery);

	}

	@RequestMapping(value = "/accounts/{id}/deliveries/{deliveryId}/threshold", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public void updateThreshold(@PathVariable("id") String accountId, @PathVariable("deliveryId") String deliveryId,
			@RequestBody Delivery delivery) {
		logger.info("changing threshold for delivery {} on account {}", deliveryId, accountId);
		delivery.setId(deliveryId);
		accountService.updateTreshold(accountId, delivery);

	}

	@RequestMapping(value = "/accounts/{id}/deliveries/{deliveryId}/state", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public void changeState(@PathVariable("id") String accountId, @PathVariable("deliveryId") String deliveryId,
			@RequestBody Delivery delivery) {
		logger.info("changing threshold for delivery {} on account {}", deliveryId, accountId);
		delivery.setId(deliveryId);
		accountService.changeState(accountId, delivery);

	}
	
	@RequestMapping(value = "/accounts/{id}/deliveries/{deliveryId}/description", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public void updateDescription(@PathVariable("id") String accountId, @PathVariable("deliveryId") String deliveryId,
			@RequestBody Delivery delivery) {
		logger.info("changing description for delivery {} on account {}", deliveryId, accountId);
		delivery.setId(deliveryId);
		accountService.updateDescription(accountId, delivery);

	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(DuplicateDeliveryException.class)
	public RestError handleDuplicateCustomer(DuplicateDeliveryException dup) {
		RestError re = new RestError();
		re.setCode(dup.getErrorCode());
		re.setMessage(dup.getMessage());
		return re;
	}
}
