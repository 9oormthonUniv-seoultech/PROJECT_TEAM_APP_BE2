package com.groomiz.billage.reservation.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.groomiz.billage.reservation.dto.request.AdminReservationRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/admin/reservations")
@Tag(name = "Admin Reservation Controller", description = "[관리자] 예약 관련 API")
@RequiredArgsConstructor
public class AdminReservationController {

	@GetMapping
	@Operation(summary = "예약 상태별 리스트 조회")
	public String getReservationsByStatus(
		@RequestParam(required = false) String status) {

		return "예약 상태별 리스트 조회 완료";
	}

	@PostMapping("/{id}/approve")
	@Operation(summary = "예약 승인")
	public String approveReservation(@PathVariable Long id) {

		return "예약 승인 되었습니다.";
	}

	@PostMapping("/{id}/reject")
	@Operation(summary = "예약 거절")
	public String rejectReservation(@PathVariable Long id,
		@RequestBody(required = false) String rejectionReason) {
		// 거절 사유가 있는 경우 문자열에 추가
		if (rejectionReason != null && !rejectionReason.isEmpty()) {
			return "예약 거절 완료되었습니다. 거절 사유: " + rejectionReason;
		}
		return "예약 거절 완료되었습니다.";
	}

	@PostMapping
	@Operation(summary = "예약 요청 처리")
	public String createReservation(@RequestBody AdminReservationRequest request) {

		return "예약 완료 하였습니다.";
	}

	@GetMapping("/{reservationId}")
	@Operation(summary = "예약 상세 조회")
	public String getReservation(@PathVariable Long reservationId) {

		return "예약 상세 조회 완료";
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "예약 삭제", description = "단일 예약, 기간 예약, 반복 예약을 삭제합니다.")
	public String deleteReservation(@PathVariable Long id,
		@RequestParam(required = false, defaultValue = "single") String type) {

		return "예약 삭제 하였습니다.";
	}
}