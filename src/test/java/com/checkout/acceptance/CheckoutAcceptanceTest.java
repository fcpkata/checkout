package com.checkout.acceptance;

//@RunWith(SpringRunner.class)
//@SpringBootTest
//@AutoConfigureMockMvc
public class CheckoutAcceptanceTest {
/*
	private final String URI = "http://localhost:8080/v1/invoice";

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

	@Test(expected = BadRequest.class)
	public void shouldThrowExceptionIfInValidBookId() throws Exception {

		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.set("Content-Type", "application/json");

		InvoiceRequest request = new InvoiceRequest();
		ShippingAddress address = new ShippingAddress("Robert", "smith", "126 York St.", "Ottawa", "ON", "CA",
				"(123)456-7890", "K1N 5T5");
		request = new InvoiceRequest("123", "Robert.smith@gmail.com", address);

		HttpEntity<InvoiceRequest> entity = new HttpEntity<>(request, requestHeaders);
		RestTemplate restTemplate = new RestTemplate();

		restTemplate.exchange(URI, HttpMethod.POST, entity, Order.class);

	}
*/
}
