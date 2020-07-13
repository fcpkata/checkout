package com.checkout.services;

import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.checkout.model.CatalogResponse;
import com.checkout.model.InvoiceRequest;
import com.checkout.model.Order;
import com.checkout.repositories.OrderRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CheckoutService {

	private OrderRepository orderRepository;

	@Autowired
	public CheckoutService(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	public CatalogResponse catalogLookupForProduct(String productId) {

		String uri = "http://catalog/catalog/v1/product/";
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");

		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		RestTemplate inventoryRestTemplate = new RestTemplate();
		ResponseEntity<CatalogResponse> response = inventoryRestTemplate.exchange(uri + productId, HttpMethod.GET,
				httpEntity, CatalogResponse.class);
		log.info("CatalogResponse >>>>> " + response.getBody());
		return response.getBody();
	}

	public Optional<Order> createInvoice(InvoiceRequest request, CatalogResponse product) {

		Optional<Order> order = orderRepository.fetchOrderByProductId(product.getId());
		if (order.isPresent()) {
			return order;
		} else {
			Order newOrder = Order.builder().Id(generateOrderId()).productId(product.getId())
					.itemName(product.getName()).customerName(request.getCustomerName()).price(product.getPrice())
					.billingAddress(request.getShippingAddress()).build();
			return orderRepository.saveOrder(newOrder);
		}

	}

	private String generateOrderId() {
		Random random = new Random();
		int ordSuffix = random.nextInt(100);
		return "ORD" + ordSuffix;

	}

}
