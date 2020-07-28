package com.checkout.services;

import java.util.Optional;
import java.util.Random;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.checkout.model.InventoryResponse;
import com.checkout.model.InvoiceRequest;
import com.checkout.model.Order;
import com.checkout.model.ProductInformation;
import com.checkout.repositories.OrderRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j

@Service
public class CheckoutService {

	private OrderRepository orderRepository;
	private RestTemplate restTemplate;

	public CheckoutService(OrderRepository orderRepository, RestTemplate restTemplate) {
		this.orderRepository = orderRepository;
		this.restTemplate = restTemplate;
	}

	public Optional<Order> createInvoice(InvoiceRequest request, ProductInformation product) {
		Optional<Order> orderResponse = Optional.empty();
		if (!StringUtils.isEmpty(product.getItem().getProductId())) {
			Order newOrder = Order.builder().Id(generateOrderId()).productId(product.getItem().getProductId())
					.itemName(product.getItem().getProductId()).customerName(request.getCustomerName()).price(product.getItem().getShippingPrice())
					.billingAddress(request.getShippingAddress()).build();
			orderResponse = orderRepository.saveOrder(newOrder);
		}
		return orderResponse;
	}
	
	public ProductInformation callInventoryService(String productId) {
		String uri = System.getProperty("inventoryService");
		ResponseEntity<InventoryResponse> product = restTemplate.getForEntity(uri + "/item/"+productId, InventoryResponse.class);
		if(product.getStatusCodeValue() == HttpStatus.NOT_FOUND.value()) 
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Product Id");
		return product.getBody().getProductInformation().get(0);
	}

	private String generateOrderId() {
		Random random = new Random();
		int ordSuffix = random.nextInt(100);
		return "ORD" + ordSuffix;

	}

	public Optional<Order> createInvoice(InvoiceRequest request) {
		ProductInformation catalogLookupForProduct = callInventoryService(request.getBookId());
		return createInvoice(request, catalogLookupForProduct);
	}

}
