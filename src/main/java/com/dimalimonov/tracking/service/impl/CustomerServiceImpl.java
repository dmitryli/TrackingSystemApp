package com.dimalimonov.tracking.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.dimalimonov.tracking.domain.Carrier;
import com.dimalimonov.tracking.domain.Customer;
import com.dimalimonov.tracking.errors.DuplicateCustomerException;
import com.dimalimonov.tracking.errors.MissingCustomerDetailsException;
import com.dimalimonov.tracking.service.AccountOrderService;
import com.dimalimonov.tracking.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {
	private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);
	private static final String EMAIL = "Email";
	private static final String DISPLAYNAME = "Display Name";
	private static final String PASSWORD = "Password";
	private static final String SEPARATOR = ", ";
	private static final String COLLECTION_NAME = "ptrackusers";
	private static final String ROLE_USER = "USER";

	@Value("${usps.threshold}")
	public Integer uspsThreshold = null;

	@Value("${ups.threshold}")
	public Integer upsThreshold = null;
	
	@Value("${notification.time}")
	public Integer emailNotificationTime = null;

	@Autowired
	private AccountOrderService accountService = null;

	@Autowired
	private MongoOperations mongoOperations = null;

	@Override
	public Customer create(Customer customer) {
		logger.info("Attepting create a customer with email {}", customer.getEmail());
		if (!StringUtils.isEmpty(customer.getEmail()) && findByEmail(customer.getEmail()) != null) {
			logger.info("Customer with email {} already exists", customer.getEmail());
			throw new DuplicateCustomerException(customer.getEmail());
		}

		if (StringUtils.isEmpty(customer.getDisplayName()) || StringUtils.isEmpty(customer.getEmail())
				|| StringUtils.isEmpty(customer.getPassword())) {
			throw new MissingCustomerDetailsException(getValidatedErrorMessage(customer));

		}

		customer.setRole(ROLE_USER);
		customer.addCarrierThreshold(Carrier.UPS, upsThreshold);
		customer.addCarrierThreshold(Carrier.USPS, uspsThreshold);
		customer.setEmailNotificationTime(emailNotificationTime);
		mongoOperations.insert(customer, COLLECTION_NAME);
		logger.info("customer with email {} and id {} has been created", customer.getEmail(), customer.getId());

		return customer;
	}

	private String getValidatedErrorMessage(Customer customer) {
		String errorResult = null;
		StringBuffer errorMessage = new StringBuffer();
		if (StringUtils.isEmpty(customer.getDisplayName())) {
			errorMessage.append(DISPLAYNAME).append(SEPARATOR);
		}
		if (StringUtils.isEmpty(customer.getEmail())) {
			errorMessage.append(EMAIL).append(SEPARATOR);
		}

		if (StringUtils.isEmpty(customer.getPassword())) {
			errorMessage.append(PASSWORD);
		}

		if (errorMessage.length() > 0) {
			errorResult = errorMessage.toString();
			if (errorResult.endsWith(SEPARATOR)) {
				errorResult = errorResult.substring(0, errorResult.lastIndexOf(SEPARATOR));
			}
		}
		return errorResult;
	}

	@Override
	public Customer findById(String id) {
		Customer c = mongoOperations.findById(id, Customer.class, COLLECTION_NAME);
		return c;
	}

	@Override
	public Customer findByEmail(String id) {
		Customer c = null;
		List<Customer> find = mongoOperations.find(Query.query(Criteria.where("email").is(id)), Customer.class,
				COLLECTION_NAME);
		if (find != null && find.size() > 0) {
			c = find.get(0);
		}
		return c;
	}

	@Override
	public Customer update(Customer customer) {
		mongoOperations.save(customer, COLLECTION_NAME);
		return customer;

	}

	@Override
	public void deleteById(String id) {
		Customer c = mongoOperations.findById(id, Customer.class, COLLECTION_NAME);
		mongoOperations.remove(c, COLLECTION_NAME);
		logger.info("deleted customer with id {}", c.getId());

	}

	@Override
	public void deleteByEmail(String id) {
		// TODO Auto-generated method stub

	}
}
