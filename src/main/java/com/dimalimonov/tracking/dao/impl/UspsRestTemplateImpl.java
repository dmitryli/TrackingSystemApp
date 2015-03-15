package com.dimalimonov.tracking.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.dimalimonov.tracking.dao.CarrierRestTemplate;
import com.dimalimonov.tracking.service.impl.UpsServiceImpl;

@Service("uspsRestTemplate")
public class UspsRestTemplateImpl extends RestTemplate implements CarrierRestTemplate {

	private static final Logger logger = LoggerFactory.getLogger(UpsServiceImpl.class);

	@Value("${usps.server.url}")
	private String serverUrl = null;

	@Value("${usps.userid}")
	private String userId = null;

	@Override
	public String getStatus(String id) {
		String trackingRequest = getTrackingRequest(id);

		logger.debug("Request URL code  {}", trackingRequest);

		UriComponents uri = UriComponentsBuilder.fromHttpUrl(serverUrl).queryParam("API", "TrackV2")
				.queryParam("XML", getTrackingRequest(id)).build();

		ResponseEntity<String> response = getForEntity(uri.toUri(), String.class);

		String responseBody = response.getBody();
		logger.debug("Response code  {}", response.getStatusCode());
		logger.debug("Response Body {}", responseBody);

		return responseBody;

	}

	private String getTrackingRequest(String id) {
		return String.format(TRACKING_REQ_TEMPLATE, userId, id);
	}

	private final String TRACKING_REQ_TEMPLATE = "<TrackRequest USERID='%s'><TrackID ID='%s'></TrackID></TrackRequest>";

}
