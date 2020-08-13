package com.checkout.integration;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.client.RestTemplate;

import com.checkout.Checkout;
import com.checkout.model.InventoryResponse;
import com.checkout.model.InvoiceRequest;
import com.checkout.model.Item;
import com.checkout.model.Product;
import com.checkout.model.ProductInformation;
import com.checkout.model.ShippingAddress;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Checkout.class)
@AutoConfigureMockMvc
public class CheckoutIntegrationTest {

	@Autowired
	private MockMvc mockMvc;
	private MockHttpServletRequestBuilder requestBuilder;
	private MockRestServiceServer mockRestServiceServer;

	@Autowired
	private RestTemplate restTemplate;

	@Before
	public void before() throws JsonProcessingException {
		mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).ignoreExpectOrder(true).build();
	}

	@Test
	public void shouldCreateInvoiceForValidBookId() throws Exception {

		mockCatalogService();
		mockInventoryService(1);

		buildRequestBuilder(getInvoiceRequest());

		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpect(jsonPath("$.productId").value(getInvoiceRequest().getBookId()));
	}

	@Test
	public void shouldReturnNoContentForIdWithNoData() throws Exception {
		mockCatalogService();
		mockInventoryServiceToReturnError();

		buildRequestBuilder(getInvoiceRequestWithInvalidId());

		mockMvc.perform(requestBuilder).andExpect(status().isNoContent());
	}

	private void mockCatalogService() throws JsonProcessingException {
		mockRestServiceServer
				.expect(ExpectedCount.manyTimes(),
						MockRestRequestMatchers.requestTo(containsString("/catalog/v1/products/")))
				.andRespond(MockRestResponseCreators.withSuccess(
						new ObjectMapper().writeValueAsString(Product.builder().name("Product").build()),
						MediaType.APPLICATION_STREAM_JSON));
	}

	private void mockInventoryService(int numberOfSellers) throws JsonProcessingException {
		mockRestServiceServer
				.expect(ExpectedCount.manyTimes(), MockRestRequestMatchers.requestTo(containsString("/v1/item/")))
				.andRespond(MockRestResponseCreators.withSuccess(
						new ObjectMapper().writeValueAsString(
								new InventoryResponse(prepareProductInformationsFor(numberOfSellers))),
						MediaType.APPLICATION_STREAM_JSON));
	}
	
	private void mockInventoryServiceToReturnError() throws JsonProcessingException {
		mockRestServiceServer
				.expect(ExpectedCount.manyTimes(), MockRestRequestMatchers.requestTo(containsString("/v1/item/")))
				.andRespond(MockRestResponseCreators.withSuccess(
						new ObjectMapper().writeValueAsString(
								new InventoryResponse(prepareProductInformationsFor())),
						MediaType.APPLICATION_STREAM_JSON));
	}


	private List<ProductInformation> prepareProductInformationsFor(int numberOfItems) {

		List<ProductInformation> productInformations;
		productInformations = new ArrayList<ProductInformation>();
		productInformations.add(ProductInformation.builder().item(Item.builder().price(100).productId("PD001").build()).build());
		return productInformations;
	}
	
	private List<ProductInformation> prepareProductInformationsFor() {

		List<ProductInformation> productInformations;
		productInformations = new ArrayList<ProductInformation>();
		return productInformations;
	}

	private void buildRequestBuilder(InvoiceRequest invoiceRequest) throws JsonProcessingException {
		this.requestBuilder = post("/v1/invoice");
		this.requestBuilder.content(new ObjectMapper().writeValueAsString(invoiceRequest));
		this.requestBuilder.contentType(MediaType.APPLICATION_JSON);
	}

	private InvoiceRequest getInvoiceRequest() {
		InvoiceRequest request = new InvoiceRequest();
		ShippingAddress address = new ShippingAddress("Robert", "smith", "126 York St.", "Ottawa", "ON", "CA",
				"(123)456-7890", "K1N 5T5");
		request = new InvoiceRequest("PD001", "Robert.smith@gmail.com", address);
		return request;
	}
	
	private InvoiceRequest getInvoiceRequestWithInvalidId() {
		InvoiceRequest request = new InvoiceRequest();
		ShippingAddress address = new ShippingAddress("Robert", "smith", "126 York St.", "Ottawa", "ON", "CA",
				"(123)456-7890", "K1N 5T5");
		request = new InvoiceRequest("123", "Robert.smith@gmail.com", address);
		return request;
	}
}
