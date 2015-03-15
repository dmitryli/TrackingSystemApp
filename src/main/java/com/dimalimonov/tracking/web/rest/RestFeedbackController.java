package com.dimalimonov.tracking.web.rest;

import java.net.URI;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dimalimonov.tracking.domain.Feedback;
import com.dimalimonov.tracking.service.impl.FeedbackService;
import com.dimalimonov.tracking.util.Constants;

@RestController
public class RestFeedbackController {

	private static final Logger logger = LoggerFactory.getLogger(RestFeedbackController.class);

	@Autowired
	private FeedbackService feedbackService = null;

	@RequestMapping(value = "/feedbacks", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Feedback> createFeedback(@RequestBody Feedback feedback) {
		Feedback f = feedbackService.create(feedback);

		HttpHeaders headers = new HttpHeaders();
		String location = String.format(Constants.FEEDBACK_URI, f.getId());
		headers.setLocation(URI.create(location));
		ResponseEntity<Feedback> re = new ResponseEntity<Feedback>(f, headers, HttpStatus.CREATED);

		return re;
	}

	@RequestMapping(value = "/feedbacks", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public List<Feedback> geAllFeedbacks() {

		return feedbackService.findAll();
	}

	@RequestMapping(value = "/feedbacks/{id}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Feedback geFeedbacks(@PathVariable("id") String id) {

		return feedbackService.findById(id);
	}

	@RequestMapping(value = "/feedbacks", method = RequestMethod.DELETE)
	public void deleteAll() {
		List<Feedback> all = feedbackService.findAll();
		for (Feedback a : all) {
			feedbackService.delete(a.getId());
		}
	}
}
