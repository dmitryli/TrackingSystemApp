package com.dimalimonov.tracking.rest.template.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.dimalimonov.tracking.rest.template.CarrierRestTemplate;

@Service("upsRestTemplate")
public class UpsRestTemplateImpl extends RestTemplate implements CarrierRestTemplate {

	private static final Logger logger = LoggerFactory.getLogger(UpsServiceImpl.class);

	@Value("${ups.server.url}")
	private String serverUrl = null;

	@Value("${ups.license.number}")
	private String licenseNumber = null;

	@Value("${ups.userid}")
	private String userId = null;

	@Value("${ups.password}")
	private String password = null;

	@Override
	public String getStatus(String id) {
		String trackingRequest = getTrackingRequest(id);

		logger.debug("Request Body {}", trackingRequest);

		ResponseEntity<String> response = postForEntity(serverUrl, trackingRequest, String.class);

		String responseBody = response.getBody();
		logger.debug("Response code  {}", response.getStatusCode());
		logger.debug("Response Body {}", responseBody);

		return responseBody;

	}

	private String getTrackingRequest(String id) {
		return String.format(TRACKING_REQ_TEMPLATE, licenseNumber, userId, password, id);
	}

	private final String TRACKING_REQ_TEMPLATE = "<?xml version=\"1.0\"?>" + "<AccessRequest>"
			+ "<AccessLicenseNumber>%s</AccessLicenseNumber>" + "<UserId>%s</UserId>" + "<Password>%s</Password>"
			+ "</AccessRequest>" + "<?xml version=\"1.0\"?>" + "<TrackRequest>" + "<Request>"
			+ "<TransactionReference>" + "<CustomerContext>simple test dima</CustomerContext>"
			+ "<XpciVersion>1.0</XpciVersion>" + "</TransactionReference>" + "<RequestAction>Track</RequestAction>"
			+ "<RequestOption>activity</RequestOption>" + "</Request>" + "<TrackingNumber>%s</TrackingNumber>"
			+ "</TrackRequest>";

}
