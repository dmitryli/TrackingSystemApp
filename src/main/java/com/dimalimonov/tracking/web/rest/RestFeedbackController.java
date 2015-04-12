package com.dimalimonov.tracking.web.rest;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dimalimonov.tracking.domain.Feedback;
import com.dimalimonov.tracking.service.FeedbackService;
import com.dimalimonov.tracking.util.Constants;

@RestController
public class RestFeedbackController {

	private static final Logger logger = LoggerFactory.getLogger(RestFeedbackController.class);

	@Autowired
	private FeedbackService feedbackService = null;

	@RequestMapping(value = "/feedbacks", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Feedback> createFeedback(@RequestBody Feedback feedback) {

		logger.info("Create Feedback from account {}: {}", feedback.getSourceAccoundId(), feedback.getText());
		Feedback f = feedbackService.create(feedback);

		HttpHeaders headers = new HttpHeaders();
		String location = String.format(Constants.FEEDBACK_URI, f.getId());
		headers.setLocation(URI.create(location));
		ResponseEntity<Feedback> re = new ResponseEntity<Feedback>(f, headers, HttpStatus.CREATED);

		return re;
	}
}
