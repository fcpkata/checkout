package com.checkout.services;

import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import com.checkout.model.InventoryResponse;
import com.checkout.model.InvoiceRequest;
import com.checkout.model.Order;
import com.checkout.model.Product;
import com.checkout.model.ProductInformation;
import com.checkout.repositories.OrderRepository;

@Service
public class CheckoutService {

	private OrderRepository orderRepository;
	private CatalogService catalogService;
	private InventoryService inventoryService;

	@Autowired
	public CheckoutService(OrderRepository orderRepository, CatalogService catalogService,
			InventoryService inventoryService) {
		this.orderRepository = orderRepository;
		this.catalogService = catalogService;
		this.inventoryService = inventoryService;
	}

	private Optional<Order> createInvoice(InvoiceRequest request, ProductInformation product, String productName) {
		Optional<Order> orderResponse = Optional.empty();
		if (!StringUtils.isEmpty(product.getItem().getProductId())) {
			Order newOrder = Order.builder().Id(generateOrderId()).productId(product.getItem().getProductId())
					.itemName(productName).customerName(request.getCustomerName())
					.shippingPrice(product.getItem().getShippingPrice()).price(product.getItem().getPrice())
					.billingAddress(request.getShippingAddress()).build();
			orderResponse = orderRepository.saveOrder(newOrder);
		}
		return orderResponse;
	}

	private ProductInformation callInventoryService(String productId) {
		String uri = System.getProperty("inventoryService");
		InventoryResponse inventoryResponse = inventoryService.getInventoryData(productId, uri);

		if (inventoryResponse.getProductInformations() == null
				|| inventoryResponse.getProductInformations().size() == 0)
			throw new ResponseStatusException(HttpStatus.NO_CONTENT);

		return inventoryResponse.getProductInformations().get(0);
	}

	private String callCatalogService(String productId) {
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
