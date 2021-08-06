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
import com.microsoft.sample.api.dal.CosmosAsyncDAL;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class AsyncApiController {
	
	private static Logger LOGGER = LoggerFactory.getLogger(AsyncApiController.class);
	private static String host = "https://rdcosmos.documents.azure.com:443/";
	private static String key = "fvlHanr6UxkLlVbQucsh65VSqvuzh6oj0PhMJerSRacy3gnwMXdo562tmBzvUHlOg3EPmkfoaSCKEkN8XUWVtg==";
	private static String databaseName = "mydb";
	private static String containerName = "mycontainer";
	
	CosmosAsyncDAL cosmosAsyncDAL = CosmosAsyncDAL.getInstance(host, key, databaseName, containerName);
	
	@PostMapping(path = "/async/v1/items", consumes = "application/json", produces = "application/json")
	public Mono<JsonNode> create(
								@RequestBody JsonNode data,
								@PathVariable String cosmosAPI) {
		return cosmosAsyncDAL.create(data);
	}
	@GetMapping(path = "/async/v1/items/{pk}/{id}", produces = "application/json")
	public JsonNode retrieve(
						@PathVariable(value = "id") String id,
						@PathVariable(value = "pk") String pk) {
		LOGGER.info("Point Query");
		return cosmosAsyncDAL.retrieve(id, pk);
	}
	
	//?filters={"attributeName1":"attributeValue1", "attributeName2":"attributeValue2"}
	@GetMapping(path = "/async/v1/items", produces = "application/json")
	public Flux<List<JsonNode>> retrievewithQuery(
						@QueryParam(value = "filters") String filters) {
		return cosmosAsyncDAL.query(filters);
	}
	
	
	@PutMapping(path = "/async/v1/items", consumes = "application/json", produces = "application/json")
	public Mono<JsonNode> update(@RequestBody JsonNode data) {
		return cosmosAsyncDAL.update(data);
	}
	
	@DeleteMapping(path = "/async/v1/items/{pk}/{id}")
	public Mono<Object> delete(
						@PathVariable("pk") String pk,
						@PathVariable("id") String id
								) {
		System.out.println("pk....." + pk);
		return cosmosAsyncDAL.delete(pk, id);
	}
	
}
