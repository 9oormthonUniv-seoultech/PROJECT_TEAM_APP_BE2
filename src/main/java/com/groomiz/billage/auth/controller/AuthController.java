package com.groomiz.billage.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.groomiz.billage.auth.dto.LoginResponse;
import com.groomiz.billage.auth.dto.RefreshTokenRequest;
import com.groomiz.billage.auth.service.RefreshTokenService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final RefreshTokenService refreshTokenService;

	@PostMapping("/refresh-token")
	public ResponseEntity<?> reissue(@RequestBody RefreshTokenRequest refreshTokenRequest) {
		try {
			LoginResponse reissue = refreshTokenService.reissue(refreshTokenRequest.getRefreshToken());
			return ResponseEntity.ok(reissue);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Reissue failed: " + e.getMessage());
		}
	}
}
