package com.checkout.repositories;

import java.util.Optional;

import com.checkout.model.Order;

public interface Repository {
	
	Optional<Order> saveOrder(Order order);

}
