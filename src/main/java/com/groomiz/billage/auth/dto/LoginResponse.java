package com.groomiz.billage.auth.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "로그인 응답 DTO")
public class LoginResponse {

	@Schema(description = "Access Token", example = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
	private String authorization;

	@Schema(description = "Refresh Token", example = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
	private String refreshToken;

	public LoginResponse(String authorization, String refreshToken) {
		this.authorization = authorization;
		this.refreshToken = refreshToken;
	}
}
