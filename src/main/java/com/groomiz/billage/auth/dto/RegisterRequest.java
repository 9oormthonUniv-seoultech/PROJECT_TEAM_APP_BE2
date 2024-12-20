package com.groomiz.billage.auth.dto;

import com.groomiz.billage.member.valid.ValidEmail;
import com.groomiz.billage.member.valid.ValidPassword;
import com.groomiz.billage.member.valid.ValidPhoneNumber;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Schema(description = "회원 가입 요청 DTO")
@AllArgsConstructor
public class RegisterRequest {

	@NotNull
	@Schema(description = "이름", example = "홍길동")
	private String name;

	@NotNull
	@Schema(description = "학번", example = "20100000")
	private String studentNumber;

	@NotNull
	@ValidPassword
	@Schema(description = "비밀번호", example = "password1234!")
	private String password;

	@NotNull
	@ValidPhoneNumber
	@Schema(description = "전화번호", example = "010-1234-5678")
	private String phoneNumber;

	@NotNull
	@Schema(description = "단과대", example = "정보통신대학")
	private String college;

	@Schema(description = "전공", example = "컴퓨터공학과")
	private String major;

	@NotNull
	@Schema(description = "약관동의", example = "true")
	private boolean agreedToTerms;

	@NotNull
	@ValidEmail
	@Schema(description = "이메일", example = "asdf1234@gmail.com")
	private String studentEmail;

}
