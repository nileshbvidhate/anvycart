package com.nv.downstream.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

	@GetMapping("/headers")
	public String testHeaders(@RequestHeader("X-User-Id") String userId, @RequestHeader("X-Username") String username,
			@RequestHeader("X-User-Role") String role) {

		System.out.println("Hello from test controller of downstream");
		return "Received X-User-* headers: " + userId + ", " + username + ", " + role;
	}

	@GetMapping("/welcome")
	public String welcome() {

		System.out.println("Hello from test controller of downstream");
		return "WELCOME-USER to, test controller of downstream";
		
	}
}
