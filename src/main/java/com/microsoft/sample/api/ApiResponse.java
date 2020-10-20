package com.microsoft.sample.api;

import com.fasterxml.jackson.databind.JsonNode;

public class ApiResponse {
	
	private JsonNode data;
	private String etag;
	private String sessionToken;
	
	public ApiResponse(JsonNode node) {
		
	}
}
