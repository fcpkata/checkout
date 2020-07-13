package com.checkout.acceptance;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.checkout.model.InvoiceRequest;
import com.checkout.model.Order;
import com.checkout.model.ShippingAddress;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class CheckoutAcceptanceTest {

	private final String URI = "http://checkout/v1/invoice";

	@Test
	public void shouldCreateInvoiceForValidBookId() throws Exception {

		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.set("Content-Type", "application/json");

		InvoiceRequest request = new InvoiceRequest();
		ShippingAddress address = new ShippingAddress("Robert", "smith", "126 York St.", "Ottawa", "ON", "CA",
				"(123)456-7890", "K1N 5T5");
		request = new InvoiceRequest("PD001", "Robert.smith@gmail.com", address);

		HttpEntity<InvoiceRequest> entity = new HttpEntity<>(request, requestHeaders);
		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<Order> result = restTemplate.exchange(URI, HttpMethod.POST, entity, Order.class);

		assertThat(result).isNotNull();
		Order response = result.getBody();
		AssertionsForClassTypes.assertThat(response).isNotNull();

	}

	@Test
	@Ignore("Need to check")
	public void shouldThrowExceptionIfInValidBookId() throws Exception {

		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.set("Content-Type", "application/json");

		InvoiceRequest request = new InvoiceRequest();
		ShippingAddress address = new ShippingAddress("Robert", "smith", "126 York St.", "Ottawa", "ON", "CA",
				"(123)456-7890", "K1N 5T5");
		request = new InvoiceRequest("123", "Robert.smith@gmail.com", address);

		HttpEntity<InvoiceRequest> entity = new HttpEntity<>(request, requestHeaders);
		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<Order> result = restTemplate.exchange(URI, HttpMethod.POST, entity, Order.class);

		log.info(">>>>>>>>>>>>>>> "+result.getStatusCode());
		assertEquals(result.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
