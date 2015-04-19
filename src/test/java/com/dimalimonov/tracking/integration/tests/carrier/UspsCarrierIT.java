package com.dimalimonov.tracking.integration.tests.carrier;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
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
public class UspsCarrierIT {

	private static final Logger logger = LoggerFactory.getLogger(UspsCarrierIT.class);
	
	@Value("classpath:usps-numbers.txt")
	private Resource uspsNumbers = null;

	@Autowired
	@Qualifier("uspsService")
	private CarrierService upsService = null;

	@Test
	public void testGetPackagesStatus() throws Exception {
		File file = uspsNumbers.getFile();
		List<String> lines = FileUtils.readLines(file);
	
		for (String s : lines) {
			List<Activity> activityList = upsService.getActivityList(s);
			logger.info("order {}", s);
			Assert.assertNotNull(activityList);
			logger.info("status {}",  activityList.get(0).getStatusDescription());
		}
		
		
		
	}

}
