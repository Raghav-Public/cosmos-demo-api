package com.microsoft.sample.api;

public class ApiResponse {
	
	private int statusCode;
	private String message;
	
	public ApiResponse(int s, String m) {
		statusCode = s;
		message = m;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
