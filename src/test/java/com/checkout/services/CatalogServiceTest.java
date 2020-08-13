package com.checkout.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.checkout.model.Product;

@RunWith(MockitoJUnitRunner.class)
public class CatalogServiceTest {

	@Mock
	private RestTemplate mockRestTemplate;

	@InjectMocks
	private CatalogService catalogService;

	@Test
	public void shouldReturnProductInfo() {
		ResponseEntity<Product> responseEntity = new ResponseEntity<Product>(Product.builder().name("Product").build(),
				HttpStatus.OK);
		when(mockRestTemplate.getForEntity("url/catalog/v1/products/123", Product.class)).thenReturn(responseEntity);
		Product product = catalogService.callCatalogService("123", "url");
		assertThat(product.getName()).isEqualTo("Product");
	}

}
