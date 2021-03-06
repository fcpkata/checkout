package com.checkout.controller;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.checkout.model.InvoiceRequest;
import com.checkout.model.Order;
import com.checkout.services.CheckoutService;

@RestController
@RequestMapping("/v1")
public class CheckoutController {

	private CheckoutService checkoutService;

	@Autowired
	public CheckoutController(CheckoutService checkoutService) {
		this.checkoutService = checkoutService;
	}

	@PostMapping(value = "/invoice")
	public ResponseEntity<Order> createInvoice(@RequestBody InvoiceRequest request) throws IOException {
		Optional<Order> order = checkoutService.createInvoice(request);
		if (order.isPresent()) {
			return new ResponseEntity<>(order.get(), HttpStatus.OK);
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid ID");
		}
	}
}
