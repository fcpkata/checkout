package com.checkout.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Optional;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException.BadRequest;

import com.checkout.model.CatalogResponse;
import com.checkout.model.Category;
import com.checkout.model.InvoiceRequest;
import com.checkout.model.Order;
import com.checkout.model.Price;
import com.checkout.model.Product;
import com.checkout.model.ShippingAddress;
import com.checkout.services.CheckoutService;

@RunWith(MockitoJUnitRunner.class)
public class CheckoutControllerTest {


	@InjectMocks
	CheckoutController checkoutController;

	@Mock
	CheckoutService checkoutService;

	Order mockOrder;
	InvoiceRequest request;
	InvoiceRequest invalidRequest;

	@Test
	public void shouldCreateOrderIfItemAvailableInInventory() throws IOException {
		mockParamsForValidProductId();
		ResponseEntity<Order> response = checkoutController.createInvoice(request);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isEqualTo(mockOrder);

	}
	
	@Test(expected = BadRequest.class)
	@Ignore
	public void shouldReturnExceptionIfProductIsInvalid() throws IOException {
		mockParamsForInvalidProductId();
		checkoutController.createInvoice(invalidRequest);
	}

	private void mockParamsForValidProductId() throws IOException {

		Category category = new Category();
		CatalogResponse catalogResponse = CatalogResponse.builder().category(category).build();

		ShippingAddress address = new ShippingAddress("Robert", "smith", "126 York St.", "Ottawa", "ON", "CA",
				"(123)456-7890", "K1N 5T5");
		request = new InvoiceRequest("PD001", "Robert.smith@gmail.com", address);

		Product product = new Product("PD001", "", "", null, null, false, null);
		mockOrder = Order.builder().Id("ORD95").productId(product.getId()).itemName("item1")
				.customerName(request.getCustomerName()).price(new Price()).billingAddress(address).build();

		when(checkoutService.catalogLookupForProduct("PD001")).thenReturn(catalogResponse);
		when(checkoutService.createInvoice(request, catalogResponse)).thenReturn(Optional.of(mockOrder));
	}
	
	private void mockParamsForInvalidProductId() throws IOException {

		CatalogResponse invalidCatalog = CatalogResponse.builder().build();

		ShippingAddress address = new ShippingAddress("Robert", "smith", "126 York St.", "Ottawa", "ON", "CA",
				"(123)456-7890", "K1N 5T5");
		invalidRequest = new InvoiceRequest("INVALID_PRODUCT_ID", "Robert.smith@gmail.com", address);

		Product product = new Product("INVALID_PRODUCT_ID", "", "", null, null, false, null);
		mockOrder = Order.builder().Id("ORD95").productId(product.getId()).itemName("item1")
				.customerName(invalidRequest.getCustomerName()).price(new Price()).billingAddress(address).build();
		when(checkoutService.catalogLookupForProduct("INVALID_PRODUCT_ID")).thenReturn(invalidCatalog);
		when(checkoutService.createInvoice(invalidRequest, invalidCatalog)).thenReturn(Optional.empty());
	
	}

}
