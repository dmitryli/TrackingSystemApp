package com.dimalimonov.tracking.integration.tests.carrier;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.dimalimonov.tracking.TrackingSystemApplication;
import com.dimalimonov.tracking.domain.Activity;
import com.dimalimonov.tracking.service.CarrierService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TrackingSystemApplication.class)
@WebAppConfiguration
@ActiveProfiles("production")
public class FedexCarrierIT {

	private static final Logger logger = LoggerFactory.getLogger(FedexCarrierIT.class);

	@Value("classpath:usps-numbers.txt")
	private Resource uspsNumbers = null;

	@Autowired
	@Qualifier("fedexService")
	private CarrierService fedexService = null;

	@Test
	public void testGetPackagesStatus() {

		List<Activity> activityList = fedexService.getActivityList("780563188808");
		Assert.assertNotNull(activityList);
		logger.info("status {}", activityList.get(0).getStatusDescription());

	}

}
