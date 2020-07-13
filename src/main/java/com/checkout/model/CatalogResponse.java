package com.checkout.model;

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
public class CatalogResponse {
	
	private String id;
	private String name;
	private String description;
	private ProductMetadata metadata;
	private Price price;
	private boolean detailsPresent;
	private Category category;

}
