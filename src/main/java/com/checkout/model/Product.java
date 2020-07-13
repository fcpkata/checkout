package com.checkout.model;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class Product {

	private String id;
	private String name;
	private String description;
	private ProductMetadata metadata;
	private Price price;
	private boolean detailsPresent;
	private Category category;
	
	public boolean hasMetadata(Map<String, String> metaDataFilters) {
		return metadata.containsAll(metaDataFilters);
	}

}