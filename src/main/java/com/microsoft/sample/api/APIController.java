package com.microsoft.sample.api;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.azure.core.annotation.PathParam;
import com.azure.core.annotation.QueryParam;
import com.fasterxml.jackson.databind.JsonNode;
import com.microsoft.sample.api.dal.CosmosDAL;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class APIController {
	
	private static Logger LOGGER = LoggerFactory.getLogger(APIController.class);
	private static String host = "https://rdsqlapi.documents.azure.com:443/";
	private static String key = "stEvrfA9Dmjbdv3a0C6AJsB9PbjJpUXzreMmf21Ru42NJuW8zSvUap4fZ85sg2ApTBtg6wQsXcrMR7vkFB585A==";
	private static String databaseName = "testdb";
	private static String containerName = "storeItems";
	
	CosmosDAL cosmosDAL = CosmosDAL.getInstance(host, key, databaseName, containerName);
	
	
	@PostMapping(path = "/v1/items", consumes = "application/json", produces = "application/json")
	public Mono<JsonNode> create(@RequestBody JsonNode data) {
		return cosmosDAL.create(data);
	}
	
	@GetMapping(path = "/v1/items/{pk}/{id}", produces = "application/json")
	public Mono<JsonNode> retrieve(
						@PathVariable(value = "id") String id,
						@PathVariable(value = "pk") String pk) {
		return cosmosDAL.retrieve(id, pk).onErrorReturn(null);
	}
	
	//?filters={"attributeName1":"attributeValue1", "attributeName2":"attributeValue2"}
	@GetMapping(path = "/v1/items", produces = "application/json")
	public Flux<List<JsonNode>> retrievewithQuery(
						@QueryParam(value = "filters") String filters) {
		return cosmosDAL.query(filters);
	}
	
	
	@PutMapping(path = "/v1/items", consumes = "application/json", produces = "application/json")
	public Mono<JsonNode> update(@RequestBody JsonNode data) {
		return cosmosDAL.update(data);
	}
	
	@DeleteMapping(path = "/v1/items/{pk}/{id}")
	public Mono<Object> delete(
						@PathVariable("pk") String pk,
						@PathVariable("id") String id
								) {
		System.out.println("pk....." + pk);
		return cosmosDAL.delete(pk, id);
	}
	
}
