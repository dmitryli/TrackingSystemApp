package com.dimalimonov.tracking.integration.tests.carrier;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.dimalimonov.tracking.TrackingSystemApplication;
import com.dimalimonov.tracking.domain.Activity;
import com.dimalimonov.tracking.service.CarrierService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TrackingSystemApplication.class)
@WebAppConfiguration
public class UpsCarrierIT {

	private static final Logger logger = LoggerFactory.getLogger(UpsCarrierIT.class);

	@Autowired
	@Qualifier("upsService")
	private CarrierService upsService = null;

	@Test
	public void testGetPackagesStatus() throws Exception {
		String deliveryNumber = "9274890104201934599884";
		List<Activity> activityList = upsService.getActivityList(deliveryNumber);
		logger.info("deliveries {}", deliveryNumber);
		Assert.assertNotNull(activityList);
		logger.info("status {}", activityList.get(0).getStatusDescription());
	}

}
