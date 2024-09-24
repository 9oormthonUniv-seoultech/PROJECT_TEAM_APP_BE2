package com.groomiz.billage.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "회원 가입 요청 DTO")
public class RegisterRequest {

	@Schema(description = "이름", example = "홍길동")
	private String name;

	@Schema(description = "학번", example = "20100000")
	private String studentNumber;

	@Schema(description = "비밀번호", example = "password1234!")
	private String password;

	@Schema(description = "전화번호", example = "010-1234-5678")
	private String phoneNumber;

	@Schema(description = "단과대", example = "정보통신대학")
	private String college;

	@Schema(description = "전공", example = "컴퓨터공학과")
	private String major;

	@Schema(description = "이메일", example = "asdf1234@gmail.com")
	private String studentEmail;

}
