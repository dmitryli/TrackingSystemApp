package com.dimalimonov.tracking.domain;

public class Link {
	private String rel = null;
	private String href = null;
	public Link() {
		
	}
	public Link(String rel, String href) {
		setRel(rel);
		setHref(href);
	}
	public String getRel() {
		return rel;
	}
	public void setRel(String rel) {
		this.rel = rel;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	
	
}
