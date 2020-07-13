package com.checkout.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {
	
	private String Id;
	private String productId;
	private String itemName;
	private String customerName;
	private Price price;
	private ShippingAddress billingAddress;
	

}
