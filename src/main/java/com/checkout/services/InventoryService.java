package com.checkout.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.checkout.model.InventoryResponse;

@Service
public class InventoryService {
	
	private RestTemplate restTemplate;

	@Autowired
	public InventoryService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public InventoryResponse getInventoryData(String productId, String uri) {
		ResponseEntity<InventoryResponse> inventoryResponse = restTemplate.getForEntity(uri + "/v1/item/" + productId,
				InventoryResponse.class);
		return inventoryResponse.getBody();
	}

}
