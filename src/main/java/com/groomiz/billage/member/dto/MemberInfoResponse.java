package com.groomiz.billage.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "회원 정보 응답 DTO")
public class MemberInfoResponse {

	@Schema(description = "학번", example = "20100000")
	private Integer studentNumber;

	@Schema(description = "이름", example = "홍길동")
	private String name;

	@Schema(description = "전화번호", example = "010-1234-5678")
	private String phoneNumber;

	@Schema(description = "단과대", example = "정보통신대학")
	private String college;

	@Schema(description = "학과", example = "컴퓨터공학과")
	private String major;

	@Schema(description = "이메일", example = "asdf1234@gmail.com")
	private String email;

	@Schema(description = "예약 횟수", example = "3")
	private Integer reservationCount;

}
