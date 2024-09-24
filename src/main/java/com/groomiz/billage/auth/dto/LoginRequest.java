package com.groomiz.billage.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "로그인 요청 DTO")
public class LoginRequest {

	@Schema(description = "학번", example = "20100000")
	private String studentNumber;

	@Schema(description = "비밀번호", example = "password1234!")
	private String password;

}
