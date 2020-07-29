package com.checkout.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Optional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import com.checkout.model.InvoiceRequest;
import com.checkout.model.Order;
import com.checkout.model.ShippingAddress;
import com.checkout.services.CheckoutService;

@RunWith(MockitoJUnitRunner.class)
public class CheckoutControllerTest {


	@InjectMocks
	CheckoutController checkoutController;

	@Mock
	CheckoutService checkoutService;
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	Order mockOrder;
	InvoiceRequest request;

	@Test
	public void shouldCreateOrderIfItemAvailableInInventory() throws IOException {
		mockParamsForValidProductId();
		
		ResponseEntity<Order> response = checkoutController.createInvoice(mockInvoiceRequest());
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}
	
	@Test
	public void shouldReturnExceptionIfProductIsInvalid() throws IOException {
		expectedException.expect(ResponseStatusException.class);
		expectedException.expectMessage("Invalid ID");
		
		checkoutController.createInvoice(mockInvoiceRequest());
	}

	private InvoiceRequest mockInvoiceRequest() {
		ShippingAddress shippingAddress = new ShippingAddress("firstName", "lastName", "address1", "city", "provinceCode", "countryCode", "phone", "zip");
		
		return new InvoiceRequest("PD001", "customerName", shippingAddress);
	}

	private void mockParamsForValidProductId() throws IOException {
		when(checkoutService.createInvoice(any(InvoiceRequest.class))).thenReturn(mockOrder());
	}

	private Optional<Order> mockOrder() {
		ShippingAddress address = new ShippingAddress("firstName", "lastName", "address1", "city", "provinceCode", "countryCode", "phone", "zip");
		Order order = new Order("1", "PD001", "itemName", "customerName", 100.00, 50.00, address);
		return Optional.of(order);
	}
}
