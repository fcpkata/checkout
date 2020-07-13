package com.checkout.controller;

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
	public ResponseEntity<Order> createInvoice(@RequestBody InvoiceRequest request) {

		CatalogResponse product = checkoutService.catalogLookupForProduct(request.getBookId());
		Optional<Order> orderDetails = checkoutService.createInvoice(request, product);
		return orderDetails.map(order -> new ResponseEntity<Order>(order, HttpStatus.OK))
				.orElse(new ResponseEntity<Order>(HttpStatus.BAD_REQUEST));
	}
}
