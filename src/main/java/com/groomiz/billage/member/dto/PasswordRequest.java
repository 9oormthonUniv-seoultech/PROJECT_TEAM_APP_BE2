package com.groomiz.billage.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "비밀번호 수정 요청 DTO")
public class PasswordRequest {

	@Schema(description = "기존 비밀번호", example = "password1234!")
	private String oldPassword;

	@Schema(description = "새로운 비밀번호", example = "newpassword1234!")
	private String newPassword;
}
