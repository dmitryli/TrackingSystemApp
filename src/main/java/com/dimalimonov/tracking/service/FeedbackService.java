package com.dimalimonov.tracking.service;

import java.util.List;

import com.dimalimonov.tracking.domain.Feedback;

public interface FeedbackService {

	public abstract Feedback create(Feedback f);

	public abstract Feedback findById(String id);

	public abstract List<Feedback> findAll();

	public abstract void delete(String id);

}