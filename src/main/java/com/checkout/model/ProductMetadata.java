package com.checkout.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.ToString;

@ToString
public class ProductMetadata {

	private HashMap<String, String> metadata;

	public ProductMetadata() {
		this.metadata = new HashMap<String, String>();
	}

	public ProductMetadata add(String key, String value) {
		this.metadata.put(key, value);
		return this;
	}

	public Map<String, String> getMetadata() {
		return new HashMap<String, String>(metadata);
	}

	public boolean containsAll(Map<String, String> metaDataFilters) {
		if(this.metadata.isEmpty()) {
			return false;
		}
		
		List<String> matchedKeys = metaDataFilters.keySet()
		.stream()
		.filter(key -> {
			String value = metadata.get(key);
			return (value != null && metaDataFilters.get(key).equals(value));
		})
		.collect(Collectors.toList());
		
		return matchedKeys.size() == metaDataFilters.size();
	}
}
