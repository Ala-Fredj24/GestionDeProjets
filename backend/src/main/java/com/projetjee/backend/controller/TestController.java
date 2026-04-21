package com.projetjee.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
	@GetMapping("/api/test/hello")
	public String hello() {
		return "Backend spring boot fontionne !";
	}
}
