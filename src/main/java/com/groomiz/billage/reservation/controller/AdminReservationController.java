package com.groomiz.billage.reservation.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.groomiz.billage.reservation.dto.request.AdminReservationRequest;
import com.groomiz.billage.reservation.dto.response.AdminReservationResponse;
import com.groomiz.billage.reservation.dto.response.AdminReservationStatusListResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/admin/reservations")
@Tag(name = "Admin Reservation Controller", description = "[관리자] 예약 관련 API")
public class AdminReservationController {

	@GetMapping
	@Operation(summary = "예약 상태별 리스트 조회")
	public ResponseEntity<List<AdminReservationStatusListResponse>> getReservationsByStatus(
		@RequestParam(required = false) String status) {
		return ResponseEntity.ok(null);
	}

	@PostMapping("/{id}/approve")
	@Operation(summary = "예약 승인")
	public ResponseEntity<Map<String, String>> approveReservation(@PathVariable Long id) {
		Map<String, String> response = new HashMap<>();
		response.put("message", "예약 승인 되었습니다.");
		return ResponseEntity.ok(response);
	}

	@PostMapping("/{id}/reject")
	@Operation(summary = "예약 거절")
	public ResponseEntity<Map<String, String>> rejectReservation(@PathVariable Long id,
		@RequestBody(required = false) String rejectionReason) {
		Map<String, String> response = new HashMap<>();
		response.put("message", "예약 거절 완료되었습니다.");
		if (rejectionReason != null && !rejectionReason.isEmpty()) {
			response.put("rejection_reason", rejectionReason);
		}
		return ResponseEntity.ok(response);
	}

	@PostMapping
	@Operation(summary = "예약 요청 처리")
	public ResponseEntity<?> createReservation(
		@RequestBody AdminReservationRequest request) {

		return ResponseEntity.ok().body("{\"message\": \"예약 완료 하였습니다.\"}");
	}

	@GetMapping("/{reservationId}")
	public ResponseEntity<AdminReservationResponse> getReservation(@PathVariable Long reservationId) {
		// 실제 로직 없이 기본 뼈대만 작성
		return ResponseEntity.ok(new AdminReservationResponse());
	}
}