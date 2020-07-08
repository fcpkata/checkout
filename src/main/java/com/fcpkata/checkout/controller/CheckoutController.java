package com.fcpkata.checkout.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class CheckoutController {
	@GetMapping("/getCheckout")
	public ResponseEntity<String> hello()   
	{ 
		return new ResponseEntity<String>("Hello There!", HttpStatus.OK);
	//return "Hello There!";  
	}
}
