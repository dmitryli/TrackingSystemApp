package com.dimalimonov.tracking.rest.template.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.dimalimonov.tracking.rest.template.CarrierRestTemplate;

@Service("fedexRestTemplate")
public class FedexRestTemplateImpl extends RestTemplate implements CarrierRestTemplate {

	private static final Logger logger = LoggerFactory.getLogger(UpsServiceImpl.class);

	@Value("${fedex.server.url}")
	private String serverUrl = null;

	@Value("${ups.license.number}")
	private String licenseNumber = null;

	@Value("${fedex.userid}")
	private String userId = null;

	@Value("${fedex.password}")
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
		return String.format(TRACKING_REQ_TEMPLATE, userId, password, id);
	}

	private final static String TRACKING_REQ_TEMPLATE = "<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:v9='http://fedex.com/ws/track/v9'>"
			+ "<soapenv:Header/><soapenv:Body><v9:TrackRequest><v9:WebAuthenticationDetail>"
			+ "<v9:UserCredential>"
			+ "<v9:Key>%s</v9:Key>"
			+ "<v9:Password>%s</v9:Password>"
			+ "</v9:UserCredential>"
			+ "</v9:WebAuthenticationDetail>"
			+ "<v9:ClientDetail>" +
				"<v9:AccountNumber>510087542</v9:AccountNumber>"+
				"<v9:MeterNumber>118678518 </v9:MeterNumber>"+
				"<v9:Localization>"+
					"<v9:LanguageCode>EN</v9:LanguageCode>"+
					"<v9:LocaleCode>US</v9:LocaleCode>"+
				"</v9:Localization>"+
			"</v9:ClientDetail>" + 
			"<v9:TransactionDetail>"+
				"<v9:CustomerTransactionId>Track By Number_v9</v9:CustomerTransactionId>"+
				"<v9:Localization>"+
					"<v9:LanguageCode>EN</v9:LanguageCode>"+
					"<v9:LocaleCode>US</v9:LocaleCode>"+
				"</v9:Localization>"+
			"</v9:TransactionDetail>"+
			"<v9:Version>"+
				"<v9:ServiceId>trck</v9:ServiceId>"+
				"<v9:Major>9</v9:Major>"+
				"<v9:Intermediate>1</v9:Intermediate>"+
				"<v9:Minor>0</v9:Minor>"+
			"</v9:Version>"
			+ "<v9:SelectionDetails><v9:CarrierCode>FDXE</v9:CarrierCode><v9:PackageIdentifier>"
			+ "<v9:Type>TRACKING_NUMBER_OR_DOORTAG</v9:Type>"
			+ "<v9:Value>%s</v9:Value>"
			+ "</v9:PackageIdentifier><v9:ShipmentAccountNumber/><v9:SecureSpodAccount/><v9:Destination/></v9:SelectionDetails></v9:TrackRequest></soapenv:Body></soapenv:Envelope>";

}
