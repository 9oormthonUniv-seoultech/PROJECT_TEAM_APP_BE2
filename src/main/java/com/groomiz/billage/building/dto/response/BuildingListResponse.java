package com.groomiz.billage.building.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "건물 목록 조회 응답 DTO")
public class BuildingListResponse {

	@Schema(description = "건물 ID", example = "1")
	private Long buildingId;
	@Schema(description = "건물 이름", example = "미래관")
	private String buildingName;
	@Schema(description = "건물 번호", example = "39")
	private String buildingNumber;

	// 생성자 추가
	public BuildingListResponse(Long buildingId, String buildingName, String buildingNumber) {
		this.buildingId = buildingId;
		this.buildingName = buildingName;
		this.buildingNumber = buildingNumber;
	}
}