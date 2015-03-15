package com.dimalimonov.tracking.domain;

public class Feedback {

	private String id = null;
	private String sourceAccoundId = null;
	private String text = null;
	private long time = 0;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getSourceAccoundId() {
		return sourceAccoundId;
	}

	public void setSourceAccoundId(String sourceAccoundId) {
		this.sourceAccoundId = sourceAccoundId;
	}

}
