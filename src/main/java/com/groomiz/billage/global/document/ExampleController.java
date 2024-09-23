package com.groomiz.billage.global.document;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/example")
@RequiredArgsConstructor
public class ExampleController {
	@GetMapping("/global")
	public void example() {
	}
}
