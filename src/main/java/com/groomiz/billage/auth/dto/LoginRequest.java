package com.groomiz.billage.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Schema(description = "로그인 요청 DTO")
@AllArgsConstructor
public class LoginRequest {

	@NotNull
	@Schema(description = "학번", example = "20100000")
	private String studentNumber;

	@NotNull
	@Schema(description = "비밀번호", example = "password1234!")
	private String password;

	@Schema(description = "FCM 토큰", example = "dlG5jjy4SvicNcWvENgF91:APA91bHSERS39latr_mu0jh1A")
	private String FCMToken;
}
