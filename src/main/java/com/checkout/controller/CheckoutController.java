package com.checkout.controller;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.checkout.model.CatalogResponse;
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

	@PostMapping(value = "/invoice", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Order> createInvoice(@RequestBody InvoiceRequest request) throws IOException {

		CatalogResponse product = checkoutService.catalogLookupForProduct(request.getBookId());
		Optional<Order> order = checkoutService.createInvoice(request, product);
		return order.map(orderDetails -> new ResponseEntity<>(orderDetails, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
	}
}
