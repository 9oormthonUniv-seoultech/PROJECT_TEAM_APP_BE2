package com.groomiz.billage.member.dto.response;

import java.util.List;

import com.groomiz.billage.member.entity.College;
import com.groomiz.billage.member.entity.Major;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "단과대/학과 목록 조회 응답 DTO")
public class CollegeListResponse {

	@Schema(description = "단과대", example = "공과대학")
	private College college;
	@Schema(description = "학과 목록", example = "[\"기계시스템디자인공학과\", \"기계자동차공학과\"]")
	private List<Major> majors;

	public CollegeListResponse(College college) {
		this.college = college;
		this.majors = Major.getMajorsByCollege(college);
	}
}
