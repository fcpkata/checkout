package com.checkout.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.checkout.model.Order;

@Component
public class OrderRepository implements Repository {

	private static List<Order> orders = new ArrayList<>();

	@Override
	public Optional<Order> fetchOrderByProductId(String productId) {
		return orders.stream().filter(order -> order.getProductId().equals(productId)).findFirst();
	}

	@Override
	public Optional<Order> saveOrder(Order order) {
		orders.add(order);
		return Optional.of(order);
	}

}
