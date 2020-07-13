package com.checkout.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.checkout.model.CatalogResponse;
import com.checkout.model.InvoiceRequest;
import com.checkout.model.Order;
import com.checkout.model.Price;
import com.checkout.model.ShippingAddress;
import com.checkout.services.CheckoutService;

@RunWith(MockitoJUnitRunner.class)
public class CheckoutControllerTest {

	private final String productId = "PD001";

	@InjectMocks
	CheckoutController checkoutController;

	@Mock
	CheckoutService checkoutService;

	InvoiceRequest request;
	Order mockOrder;

	@Before
	public void setup() {
		mockParams();
	}

	@Test
	public void shouldCreateOrderIfItemAvailableInInventory() {

		ResponseEntity<Order> response = checkoutController.createInvoice(request);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isEqualTo(mockOrder);

	}

	private void mockParams() {

		CatalogResponse catalogResponse = CatalogResponse.builder().build();

		ShippingAddress address = new ShippingAddress("Robert", "smith", "126 York St.", "Ottawa", "ON", "CA",
				"(123)456-7890", "K1N 5T5");
		request = new InvoiceRequest(productId, "Robert.smith@gmail.com", address);

		mockOrder = Order.builder().Id("ORD95").productId("PD001").itemName("item1")
				.customerName(request.getCustomerName()).price(new Price()).billingAddress(address).build();

		when(checkoutService.catalogLookupForProduct(productId)).thenReturn(catalogResponse);
		when(checkoutService.createInvoice(Mockito.any(), Mockito.any())).thenReturn(Optional.of(mockOrder));
	}

}
