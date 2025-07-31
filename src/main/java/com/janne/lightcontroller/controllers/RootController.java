package com.janne.lightcontroller.controllers;

import ch.bildspur.artnet.ArtNetClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;

@RestController
public class RootController {

	@GetMapping("/")
	public ResponseEntity<String> root() throws UnknownHostException {
		return ResponseEntity.ok("API Root: /api/v1/");
	}

}
