package com.microsoft.sample.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.microsoft.sample.api.dal.CosmosDAL;

import reactor.core.publisher.Mono;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class APIController {
	
	private static Logger LOGGER = LoggerFactory.getLogger(APIController.class);
	private static String host = "https://rdsqlapi.documents.azure.com:443/";
	private static String key = "jbQ0aoRzvzgDqDIrC506GMhRwE2adhdNrJA0BcXPhysrwYJki7lSvvRrhCnZTS7ICAOnmG3Jve5ulRIUH4b8TA==";
	private static String databaseName = "testdb";
	private static String containerName = "storeItems";
	
	CosmosDAL cosmosDAL = CosmosDAL.getInstance(host, key, databaseName, containerName);
	
	
	@PostMapping(path = "/v1/items", consumes = "application/json", produces = "application/json")
	public Mono<ApiResponse> create(@RequestBody JsonNode data) {
		return cosmosDAL.create(data);
	}
	
	@GetMapping(path = "/v1/items", produces = "application/json")
	public Mono<JsonNode> retrieve(
						@RequestParam(value = "id") String id,
						@RequestParam(value = "pk") String pk) {
		return cosmosDAL.retrieve(id, pk);
	}
	
	@PutMapping(path = "/v1/items", consumes = "application/json", produces = "application/json")
	public Mono<ApiResponse> update(@RequestBody JsonNode data) {
		return cosmosDAL.update(data);
	}
}
