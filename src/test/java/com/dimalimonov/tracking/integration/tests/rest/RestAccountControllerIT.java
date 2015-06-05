package com.dimalimonov.tracking.integration.tests.rest;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestTemplate;

import com.dimalimonov.tracking.TrackingSystemApplication;
import com.dimalimonov.tracking.domain.Account;
import com.dimalimonov.tracking.domain.User;
import com.dimalimonov.tracking.util.PTrackIUrlService;
import com.dimalimonov.tracking.util.PTrackIUrlServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TrackingSystemApplication.class)
@WebAppConfiguration
@ActiveProfiles("production")

public class RestAccountControllerIT {

	private static final Logger logger = LoggerFactory.getLogger(RestAccountControllerIT.class);

	@Autowired
	private PTrackIUrlService pTrackIUrlService = null;
	
	private RestTemplate template = new TestRestTemplate();

	@Test
	public void testCreateReadDeleteAccount() throws Exception {
		Account a = new Account();

		HttpHeaders headers = new HttpHeaders();

//		String plainCreds = "dmitryli@outlook.com:integration";
//		byte[] plainCredsBytes = plainCreds.getBytes();
//		byte[] base64CredsBytes = Base64Utils.encode(plainCredsBytes);
//		String base64Creds = new String(base64CredsBytes);
//
//		logger.info("encoded u/p {}", base64Creds);
//		headers.add("Authorization", "Basic " + base64Creds);
//		HttpEntity<User> entity = new HttpEntity<User>(a, headers);
		
		// Create brand new account
		ResponseEntity<Account> response = template.postForEntity(pTrackIUrlService.getAccountsURI(), a, Account.class);

		Assert.assertNotNull(response.getHeaders().getLocation());
		String uri = response.getHeaders().getLocation().toString();
		logger.info("Account created at {}", uri);

		Account body = response.getBody();
		Assert.assertNotNull(body);

		String composedUri = pTrackIUrlService.getSingleAccountsURI(body.getId());
		Assert.assertEquals(composedUri, uri);
		
		// Find created account
		Account found = template.getForObject(uri, Account.class);
		Assert.assertEquals(body.getId(), found.getId());

		// Delete account
		template.delete(uri);
		
	}

}
