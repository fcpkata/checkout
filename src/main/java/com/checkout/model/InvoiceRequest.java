package com.checkout.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceRequest {
	
	private String bookId;
	private String customerName;
	private ShippingAddress shippingAddress;

}
