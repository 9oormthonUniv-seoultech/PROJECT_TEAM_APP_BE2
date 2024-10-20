package com.groomiz.billage.reservation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;

@Data
@Schema(description = "예약 거절 요청 DTO")
public class AdminRejectionRequest {

	@Schema(description = "예약 거절 사유", example = "목적이 확인되지 않습니다.")
	private String reason;
}
