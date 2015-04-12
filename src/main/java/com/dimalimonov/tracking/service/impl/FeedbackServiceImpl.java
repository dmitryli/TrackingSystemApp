package com.dimalimonov.tracking.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

import com.dimalimonov.tracking.domain.Feedback;
import com.dimalimonov.tracking.service.FeedbackService;

@Service("feedbackService")
public class FeedbackServiceImpl implements FeedbackService {
	private static final Logger logger = LoggerFactory.getLogger(FeedbackServiceImpl.class);
	private static final String COLLECTION_NAME = "feedbacks";

	@Autowired
	private MongoOperations mongoOperations = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dimalimonov.tracking.service.impl.FeedbackService#create(com.dimalimonov
	 * .tracking.domain.Feedback)
	 */
	@Override
	public Feedback create(Feedback f) {
		f.setTime(System.currentTimeMillis());
		mongoOperations.insert(f, COLLECTION_NAME);
		logger.info("new id is {}", f.getId());
		return f;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dimalimonov.tracking.service.impl.FeedbackService#findById(java.lang
	 * .String)
	 */
	@Override
	public Feedback findById(String id) {
		Feedback f = mongoOperations.findById(id, Feedback.class, COLLECTION_NAME);
		return f;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dimalimonov.tracking.service.impl.FeedbackService#findAll()
	 */
	@Override
	public List<Feedback> findAll() {
		List<Feedback> feedbacks = mongoOperations.findAll(Feedback.class, COLLECTION_NAME);
		return feedbacks;
	}

	@Override
	public void delete(String id) {

		Feedback f = mongoOperations.findById(id, Feedback.class, COLLECTION_NAME);
		mongoOperations.remove(f, COLLECTION_NAME);

	}

}
