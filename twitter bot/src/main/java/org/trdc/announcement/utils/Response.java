package org.trdc.announcement.utils;

import java.util.Map;
/**
 * Author Ibrahim Jamali
 */
public class Response {

	private String response;
	private Map<String, String> headers;
	private String sessionId;

	public Response() {
	}

	public Response(String response, Map<String, String> headers, String sessionId) {
		super();
		this.response = response;
		this.headers = headers;
		this.sessionId = sessionId;
	}

	public Response(String response, Map<String, String> headers) {
		this(response, headers, null);
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	@Override
	public String toString() {
		return "Response [response=" + response + ", headers=" + headers + ", sessionId=" + sessionId + "]";
	}

}
