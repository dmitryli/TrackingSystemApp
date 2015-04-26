package com.dimalimonov.tracking.ut.account;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.dimalimonov.tracking.TrackingSystemApplication;
import com.dimalimonov.tracking.domain.Account;
import com.dimalimonov.tracking.domain.Carrier;
import com.dimalimonov.tracking.domain.Delivery;
import com.dimalimonov.tracking.service.AccountDeliveriesService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TrackingSystemApplication.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class DeliveriesLoadTest {

	private static final Logger logger = LoggerFactory.getLogger(DeliveriesLoadTest.class);

	@Value("classpath:usps-numbers.txt")
	private Resource uspsNumbers = null;

	@Autowired
	private AccountDeliveriesService accountOrderService = null;

	@Test
	public void loadMulipleOrdersToAccount() {
		logger.info("Loading usps-numbers.txt");
		Assert.assertNotNull(uspsNumbers);
		try {
			File file = uspsNumbers.getFile();
			List<String> lines = FileUtils.readLines(file);
			Assert.assertNotNull(lines);
			Assert.assertEquals(lines.size(), 52);

			List<Delivery> list = new LinkedList<Delivery>();

			for (String s : lines) {
				Delivery o = new Delivery();
				o.setCarrier(Carrier.USPS);
				o.setId(s);
				list.add(o);
			}

			Account account = new Account();
			account = accountOrderService.createAccount(account);
			Assert.assertNotNull(account.getId());

			accountOrderService.createDeliveries(account.getId(), list);

			Assert.assertEquals(accountOrderService.findAccount(account.getId()).getDeliveries().size(), lines.size());

			accountOrderService.deleteAccount(account.getId());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
