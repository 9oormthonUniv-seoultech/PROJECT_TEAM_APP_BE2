package com.groomiz.billage.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "회원 정보 수정 요청 DTO")
public class MemberInfoRequest {

	@Schema(description = "전화번호", example = "010-1234-5678")
	private String phoneNumber;

	@Schema(description = "이메일", example = "asdf1234@gmail.com")
	private String email;

}
