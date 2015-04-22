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
import com.dimalimonov.tracking.domain.User;
import com.dimalimonov.tracking.errors.DuplicateUserException;
import com.dimalimonov.tracking.errors.MissingUserDetailsException;
import com.dimalimonov.tracking.service.AccountDeliveriesService;
import com.dimalimonov.tracking.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
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
	private AccountDeliveriesService accountService = null;

	@Autowired
	private MongoOperations mongoOperations = null;

	@Override
	public User create(User user) {
		logger.info("Attepting create a user with email {}", user.getEmail());
		if (!StringUtils.isEmpty(user.getEmail()) && findByEmail(user.getEmail()) != null) {
			logger.info("user with email {} already exists", user.getEmail());
			throw new DuplicateUserException(user.getEmail());
		}

		if (StringUtils.isEmpty(user.getDisplayName()) || StringUtils.isEmpty(user.getEmail())
				|| StringUtils.isEmpty(user.getPassword())) {
			throw new MissingUserDetailsException(getValidatedErrorMessage(user));

		}

		user.setRole(ROLE_USER);
		user.addCarrierThreshold(Carrier.UPS, upsThreshold);
		user.addCarrierThreshold(Carrier.USPS, uspsThreshold);
		user.setEmailNotificationTime(emailNotificationTime);
		mongoOperations.insert(user, COLLECTION_NAME);
		logger.info("user with email {} and id {} has been created", user.getEmail(), user.getId());

		return user;
	}

	private String getValidatedErrorMessage(User user) {
		String errorResult = null;
		StringBuffer errorMessage = new StringBuffer();
		if (StringUtils.isEmpty(user.getDisplayName())) {
			errorMessage.append(DISPLAYNAME).append(SEPARATOR);
		}
		if (StringUtils.isEmpty(user.getEmail())) {
			errorMessage.append(EMAIL).append(SEPARATOR);
		}

		if (StringUtils.isEmpty(user.getPassword())) {
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
	public User findById(String id) {
		User c = mongoOperations.findById(id, User.class, COLLECTION_NAME);
		return c;
	}

	@Override
	public User findByEmail(String id) {
		User c = null;
		List<User> find = mongoOperations
				.find(Query.query(Criteria.where("email").is(id)), User.class, COLLECTION_NAME);
		if (find != null && find.size() > 0) {
			c = find.get(0);
		}
		return c;
	}

	@Override
	public User update(User user) {
		mongoOperations.save(user, COLLECTION_NAME);
		return user;

	}

	@Override
	public void deleteById(String id) {
		User c = mongoOperations.findById(id, User.class, COLLECTION_NAME);
		mongoOperations.remove(c, COLLECTION_NAME);
		logger.info("deleted user with id {}", c.getId());

	}

	@Override
	public void deleteByEmail(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<User> find() {
		List<User> users = mongoOperations.findAll(User.class, COLLECTION_NAME);
		return users;
	}
}
