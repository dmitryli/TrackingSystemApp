package com.dimalimonov.tracking.integration.tests.util;

import java.net.URI;

import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.util.Base64Utils;

public class TestsHelper {

	public static HttpHeaders getAuthHeaders(String user, String password) {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic " + basicEncoding(user, password));

		return headers;
	}

	public static String basicEncoding(String user, String password) {
		String plainCreds = user + ":" + password;
		byte[] plainCredsBytes = plainCreds.getBytes();
		byte[] base64CredsBytes = Base64Utils.encode(plainCredsBytes);
		String base64Creds = new String(base64CredsBytes);

		return "Basic " + base64Creds;

	}
}
