package com.dimalimonov.tracking.ut.account;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.dimalimonov.tracking.TrackingSystemApplication;
import com.dimalimonov.tracking.domain.Account;
import com.dimalimonov.tracking.service.AccountOrderService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TrackingSystemApplication.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class AccountOrderServiceTest {

	private static final Logger logger = LoggerFactory.getLogger(AccountOrderServiceTest.class);

	@Autowired
	private AccountOrderService accountOrderService = null;

	@Test
	public void createAccount() {
		Account account = new Account();
		Account result = accountOrderService.createAccount(account);
		Assert.assertNotNull(result.getId());

		accountOrderService.deleteAccount(result.getId());
	}

	@Test
	public void findAccount() {
		Account account = new Account();
		Account result = accountOrderService.createAccount(account);
		Assert.assertNotNull(result.getId());

		Account find = accountOrderService.findAccount(result.getId());
		Assert.assertNotNull(find);

		accountOrderService.deleteAccount(result.getId());

	}

	@Test
	public void findAllAccounts() {
		Account account = new Account();
		Account result = accountOrderService.createAccount(account);
		Assert.assertNotNull(result.getId());

		Account account2 = new Account();
		Account result2 = accountOrderService.createAccount(account2);
		Assert.assertNotNull(result2.getId());

		List<Account> all = accountOrderService.findAccounts();
		Assert.assertNotNull(all);
		Assert.assertTrue(all.size() == 2);
		Assert.assertTrue(all.get(0).getId().equals(result.getId()));
		Assert.assertTrue(all.get(1).getId().equals(result2.getId()));

		accountOrderService.deleteAccount(result.getId());
		accountOrderService.deleteAccount(result2.getId());

	}

	@Test
	public void deleteAccount() {
		Account account = new Account();
		Account result = accountOrderService.createAccount(account);
		Assert.assertNotNull(result.getId());
		accountOrderService.deleteAccount(result.getId());
		Account find = accountOrderService.findAccount(result.getId());
		Assert.assertNull(find);

	}

}
