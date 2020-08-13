package com.checkout.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.checkout.model.Product;

@Service
public class CatalogService {
	private RestTemplate restTemplate;

	@Autowired
	public CatalogService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public Product callCatalogService(String productId, String uri) {
		ResponseEntity<Product> product = restTemplate.getForEntity(uri+"/catalog/v1/products/" + productId, Product.class);
		return product.getBody();
	}


}
