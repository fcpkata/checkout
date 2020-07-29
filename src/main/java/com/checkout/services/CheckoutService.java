package com.checkout.services;

import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.checkout.model.InventoryResponse;
import com.checkout.model.InvoiceRequest;
import com.checkout.model.Order;
import com.checkout.model.Product;
import com.checkout.model.ProductInformation;
import com.checkout.repositories.OrderRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j

@Service
public class CheckoutService {

	private OrderRepository orderRepository;
	private RestTemplate restTemplate;
	private CatalogService catalogService;

	@Autowired
	public CheckoutService(OrderRepository orderRepository, RestTemplate restTemplate, CatalogService catalogService) {
		this.orderRepository = orderRepository;
		this.restTemplate = restTemplate;
		this.catalogService = catalogService;
	}

	public Optional<Order> createInvoice(InvoiceRequest request, ProductInformation product, String productName) {
		Optional<Order> orderResponse = Optional.empty();
		if (!StringUtils.isEmpty(product.getItem().getProductId())) {
			Order newOrder = Order.builder().Id(generateOrderId()).productId(product.getItem().getProductId())
					.itemName(productName).customerName(request.getCustomerName()).shippingPrice(product.getItem().getShippingPrice()).price(product.getItem().getPrice())
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
	
	public String callCatalogService(String productId) {
		String uri = System.getProperty("catalogService");
		Product product = catalogService.callCatalogService(productId, uri);
		return product.getName();
		
	}

	private String generateOrderId() {
		Random random = new Random();
		int ordSuffix = random.nextInt(100);
		return "ORD" + ordSuffix;

	}

	public Optional<Order> createInvoice(InvoiceRequest request) {
		ProductInformation catalogLookupForProduct = callInventoryService(request.getBookId());
		return createInvoice(request, catalogLookupForProduct, callCatalogService(request.getBookId()));
		
	}

}
