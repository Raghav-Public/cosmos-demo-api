package com.microsoft.sample.api.dal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.azure.cosmos.ConsistencyLevel;
import com.azure.cosmos.CosmosAsyncClient;
import com.azure.cosmos.CosmosAsyncContainer;
import com.azure.cosmos.CosmosAsyncDatabase;
import com.azure.cosmos.CosmosClientBuilder;
import com.azure.cosmos.CosmosException;
import com.azure.cosmos.implementation.ConflictException;
import com.azure.cosmos.models.CosmosItemResponse;
import com.azure.cosmos.models.CosmosQueryRequestOptions;
import com.azure.cosmos.models.PartitionKey;
import com.azure.cosmos.util.CosmosPagedFlux;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.microsoft.sample.api.APIController;
import com.microsoft.sample.api.ApiResponse;
import com.microsoft.sample.api.helpers.ConnectionHelper;
import com.microsoft.sample.api.helpers.GenericHelper;

import reactor.core.publisher.Mono;

public class CosmosDAL {
	private String host;
	private String key;
	private String databaseName;
	private String containerName;
	private CosmosAsyncClient client;
	private CosmosAsyncDatabase database;
	private CosmosAsyncContainer container;
	
	private static Logger LOGGER = LoggerFactory.getLogger(CosmosDAL.class);
	// Singleton 
	private static CosmosDAL cosmosDAL = null;
	
	public static CosmosDAL getInstance(String host, String key, String databaseName, String containerName) {
		if(cosmosDAL == null) {
			cosmosDAL = new CosmosDAL(host, key, databaseName, containerName);
			cosmosDAL.init();
		}
		return cosmosDAL;
	}
	
	
	private CosmosDAL(String host, String key, String dbName, String cName) {
		this.host = host;
		this.key = key;
		this.databaseName = dbName;
		this.containerName = cName;
	}
	
	private Boolean init() {
		try {
			/* Direct Mode Connection Mode Client
			 * Will also need Gateway Mode config
			 * Session consistency is used, adjust the same accordingly
			 * TODO: add all the different initialization options
			 */
			client = new CosmosClientBuilder()
	        		.endpoint(host)
	        		.key(key)
	        		.directMode(ConnectionHelper.getDirectModeConfig())
	        		.gatewayMode(ConnectionHelper.getGatewayConfig())
	        		.consistencyLevel(ConsistencyLevel.SESSION)
	        		.userAgentSuffix(GenericHelper.getCurrentComputeIdentifier())
	        		.buildAsyncClient();
			LOGGER.info("Async cosmos client initiated");
			
			// assuming database was created using portal or cli
			database = client.getDatabase(databaseName);
			
			// assuming container was created using portal or cli
			container = database.getContainer(containerName);
		}
		catch(Exception exp) {
			LOGGER.error(exp.getMessage());
			return false;
		}
		return true;
	}
	
	public Mono<ApiResponse> create(JsonNode data) {
		Mono<CosmosItemResponse<JsonNode>> itemResponse = container.createItem(data);
		return itemResponse.flatMap(ir -> {
			GenericHelper.logDiagnosticsRUcharges(LOGGER, ir);
			return Mono.just(new ApiResponse(201, "Item Created"));
		});
	}
	
	/*
	 * Point Read
	 */
	public Mono<JsonNode> retrieve(String id, String pk) {
		Mono<CosmosItemResponse<JsonNode>> itemResponse = container.readItem(id, new PartitionKey(pk), JsonNode.class);
		return itemResponse.flatMap(ir -> {
			GenericHelper.logDiagnosticsRUcharges(LOGGER, ir);
			return Mono.just(ir.getItem());
		});
	}
	
	/*
	 * Query option
	 */
	/*public void query(String query) {
		CosmosQueryRequestOptions queryOptions = new CosmosQueryRequestOptions();
		queryOptions.setQueryMetricsEnabled(true);
		
		CosmosPagedFlux<JsonNode> itemPages = container.queryItems(query, queryOptions, JsonNode.class);
		itemPages.fl
		
	}*/
	
	public Mono<ApiResponse> update(JsonNode data) {
		Mono<CosmosItemResponse<JsonNode>> itemResponse = container.upsertItem(data);
		return itemResponse.flatMap(ir -> {
			GenericHelper.logDiagnosticsRUcharges(LOGGER, ir);
			return Mono.just(new ApiResponse(201, "Item Updated"));
		});
	}
	
	
}
