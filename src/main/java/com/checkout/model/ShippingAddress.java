package com.checkout.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShippingAddress {

	private String firstName;
	private String lastName;
	private String address1;
	private String city;
	private String provinceCode;
	private String countryCode;
	private String phone;
	private String zip;

}
