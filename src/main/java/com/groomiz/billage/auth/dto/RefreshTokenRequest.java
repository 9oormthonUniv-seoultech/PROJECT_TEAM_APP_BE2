package com.groomiz.billage.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Refresh Token 요청 DTO")
public class RefreshTokenRequest {

	@Schema(description = "Refresh Token", example = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
	private String refreshToken;
}
