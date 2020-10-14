package com.microsoft.sample.api.helpers;

import java.net.InetAddress;
import java.util.UUID;

import org.slf4j.Logger;

import com.azure.cosmos.models.CosmosItemResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GenericHelper {
	
	public static String getCurrentComputeIdentifier() {		
		// This is used to identify the current compute unit. can be a predetermined value 
		return UUID.randomUUID().toString();
	}
	
	public static void logDiagnosticsRUcharges(Logger logger, CosmosItemResponse<JsonNode> itemResponse) {
		logger.info("Diagnostics: " + itemResponse.getDiagnostics().toString());
		logger.info("RU Charges: " + itemResponse.getRequestCharge());
		logger.info("Session Token: " + itemResponse.getSessionToken());
	}
	
	public static JsonNode getErrorJson(Exception exp, int statusCode) {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode node = null;
		try {
			node = objectMapper.readTree("{ \"status\":" + statusCode + ", \"message\":" + exp.getMessage() + "}");
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return node;
	}
}
