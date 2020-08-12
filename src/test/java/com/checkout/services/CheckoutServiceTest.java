package com.checkout.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.checkout.model.InventoryResponse;
import com.checkout.model.InvoiceRequest;
import com.checkout.model.Item;
import com.checkout.model.Order;
import com.checkout.model.Product;
import com.checkout.model.ProductInformation;
import com.checkout.model.ShippingAddress;
import com.checkout.repositories.OrderRepository;

@RunWith(MockitoJUnitRunner.class)
public class CheckoutServiceTest {

	@InjectMocks
	private CheckoutService checkoutService;

	@Mock
	private InventoryService inventoryService;

	@Mock
	private CatalogService catalogService;

	@Mock
	private OrderRepository orderRepository;

	@Before
	public void setUp() {
		System.setProperty("inventoryService", "url");
		System.setProperty("catalogService", "url");
	}

	@After
	public void tearDown() {
		System.clearProperty("inventoryService");
		System.clearProperty("catalogService");
	}

	@Test
	public void shouldCreateInvoiceForValidProductId() {

		InvoiceRequest checkoutServiceRequest = getCheckoutServiceRequest();

		mockInventoryService(checkoutServiceRequest);
		mockCatalogService(checkoutServiceRequest);
		mockOrderRepository();

		Optional<Order> createInvoice = checkoutService.createInvoice(checkoutServiceRequest);

		assertTrue(createInvoice.isPresent());
		assertThat(createInvoice.get().getProductId()).isEqualTo("PD001");

	}

	private void mockOrderRepository() {
		Order newOrder = Order.builder().Id("123").productId("PD001").itemName("ProductName").customerName("John")
				.shippingPrice(50).price(100).billingAddress(ShippingAddress.builder().address1("Nowhere").build())
				.build();

		when(orderRepository.saveOrder(any(Order.class))).thenReturn(Optional.of(newOrder));
	}

	private void mockCatalogService(InvoiceRequest checkoutServiceRequest) {
		when(catalogService.callCatalogService(checkoutServiceRequest.getBookId(), "url"))
				.thenReturn(Product.builder().name("ProductName").build());
	}

	private void mockInventoryService(InvoiceRequest checkoutServiceRequest) {
		List<ProductInformation> productInformations;
		productInformations = new ArrayList<ProductInformation>();
		productInformations
				.add(ProductInformation.builder().item(Item.builder().price(100).productId("PD001").build()).build());
		InventoryResponse inventoryResponse = InventoryResponse.builder().productInformations(productInformations)
				.build();
		when(inventoryService.getInventoryData(checkoutServiceRequest.getBookId(), "url"))
				.thenReturn(inventoryResponse);
	}

	private InvoiceRequest getCheckoutServiceRequest() {
		return InvoiceRequest.builder().bookId("PD001").customerName("John")
				.shippingAddress(ShippingAddress.builder().address1("Nowhere").build()).build();
	}

}
