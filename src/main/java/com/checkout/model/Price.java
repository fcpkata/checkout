package com.checkout.model;

import java.util.Currency;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Price {
	private double value;
	private Currency currency;
	
	public static Price prepareINRPriceFor(double value) {
		return Price.builder().currency(Currency.getInstance("INR")).value(value).build();
	}
	
	public static Price prepareUSDPriceFor(double value) {
		return Price.builder().currency(Currency.getInstance("USD")).value(value).build();
	}
	
}
