package com.dimalimonov.tracking.account.tests;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.dimalimonov.tracking.TrackingSystemApplication;
import com.dimalimonov.tracking.domain.Account;
import com.dimalimonov.tracking.service.AccountOrderService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TrackingSystemApplication.class)
@WebAppConfiguration
public class NegativeAccountServiceTest {

	private static final Logger logger = LoggerFactory.getLogger(NegativeAccountServiceTest.class);

	@Autowired
	private AccountOrderService accountService = null;

	@Test
	public void findIllegalAccount() {

		Account result = accountService.findAccount("vasya");
		Assert.assertNull(result);
	}

}
