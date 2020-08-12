package com.checkout.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.checkout.model.InventoryResponse;
import com.checkout.model.Item;
import com.checkout.model.ProductInformation;

@RunWith(MockitoJUnitRunner.class)
public class InventoryServiceTest {

	@Mock
	private RestTemplate mockRestTemplate;

	@InjectMocks
	private InventoryService inventoryService;

	@Test
	public void shouldReturnPriceForProducts() {
		List<ProductInformation> productInformations;
		productInformations = new ArrayList<ProductInformation>();
		productInformations.add(ProductInformation.builder().item(Item.builder().price(100).build()).build());
		ResponseEntity<InventoryResponse> responseEntity = new ResponseEntity<InventoryResponse>(
				InventoryResponse.builder().productInformations(productInformations).build(), HttpStatus.OK);
		when(mockRestTemplate.getForEntity("url/v1/item/123", InventoryResponse.class)).thenReturn(responseEntity);
		InventoryResponse productPrice = inventoryService.getInventoryData("123", "url");
		assertThat(productPrice.getProductInformations().get(0).getItem().getPrice()).isEqualTo(100);
	}

}
