package com.groomiz.billage.member.dto.response;

import java.util.List;

import com.groomiz.billage.member.entity.College;
import com.groomiz.billage.member.entity.Major;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "담당자 목록 조회 응답 DTO")
public class AdminListResponse {

	@Schema(description = "단과대 이름", example = "공과대학")
	private College college;

	@Schema(description = "단과대 전화번호", example = "02-1234-5678")
	private String collegePhoneNumber;

	@Schema(description = "담당자 목록")
	private List<AdminInfo> admins;

	public AdminListResponse(College college, String collegePhoneNumber, List<AdminInfo> admins) {
		this.college = college;
		this.collegePhoneNumber = collegePhoneNumber;
		this.admins = admins;
	}

	@Data
	@Schema(description = "담당자 정보")
	public static class AdminInfo {  // 접근 제한자를 public으로 수정
		@Schema(description = "소속 학과", example = "기계시스템디자인공학과")
		private Major major;

		@Schema(description = "담당자 전화번호", example = "02-2345-6789")
		private String phoneNumber;

		public AdminInfo(Major major, String phoneNumber) {
			this.major = major;
			this.phoneNumber = phoneNumber;
		}
	}
}