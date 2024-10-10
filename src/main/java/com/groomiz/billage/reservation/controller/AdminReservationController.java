package com.groomiz.billage.reservation.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.groomiz.billage.classroom.dto.response.AdminReservationReasonResponse;
import com.groomiz.billage.common.dto.StringResponseDto;
import com.groomiz.billage.global.anotation.ApiErrorExceptionsExample;
import com.groomiz.billage.reservation.document.AdminApproveRejectExceptionDocs;
import com.groomiz.billage.reservation.document.AdminReservationDeleteExceptionDocs;
import com.groomiz.billage.reservation.document.AdminReservationExceptionDocs;
import com.groomiz.billage.reservation.document.AdminSearchExceptionDocs;
import com.groomiz.billage.reservation.document.AdminbyStatusExceptionDocs;
import com.groomiz.billage.reservation.dto.request.AdminReservationRequest;
import com.groomiz.billage.reservation.dto.response.AdminReservationStatusListResponse;

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
	@ApiErrorExceptionsExample(AdminbyStatusExceptionDocs.class)
	public ResponseEntity<List<AdminReservationStatusListResponse>> getReservationsByStatus(
		@RequestParam(required = false) String status) {
		return ResponseEntity.ok(null);
	}

	@PostMapping("/{id}/approve")
	@Operation(summary = "예약 승인")
	@ApiErrorExceptionsExample(AdminApproveRejectExceptionDocs.class)
	public ResponseEntity<StringResponseDto> approveReservation(@PathVariable Long id) {
		return ResponseEntity.ok(new StringResponseDto("예약 승인 완료되었습니다."));
	}

	@PostMapping("/{id}/reject")
	@Operation(summary = "예약 거절")
	@ApiErrorExceptionsExample(AdminApproveRejectExceptionDocs.class)
	//TODO: 이거 Reason 필요하면 해당 dto 만들어서 보내주세요! 임시라서 이렇게 생긴 거 같아 일단 두겠습니다!
	public ResponseEntity<AdminReservationReasonResponse> rejectReservation(@PathVariable Long id,
		@RequestBody(required = false) String rejectionReason) {
		return ResponseEntity.ok(new AdminReservationReasonResponse("예약 거절 완료되었습니다.", rejectionReason));
	}

	@PostMapping
	@Operation(summary = "예약 요청 처리")
	@ApiErrorExceptionsExample(AdminReservationExceptionDocs.class)
	public ResponseEntity<StringResponseDto> createReservation(
		@RequestBody AdminReservationRequest request) {
		return ResponseEntity.ok(new StringResponseDto("예약 완료 하였습니다."));
	}

	@GetMapping("/{reservationId}")
	@Operation(summary = "예약 상세 조회")
	@ApiErrorExceptionsExample(AdminSearchExceptionDocs.class)
	public ResponseEntity<StringResponseDto> getReservation(@PathVariable Long reservationId) {
		return ResponseEntity.ok(null);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "예약 삭제", description = "단일 예약, 기간 예약, 반복 예약을 삭제합니다.")

	@ApiErrorExceptionsExample(AdminReservationDeleteExceptionDocs.class)
	public ResponseEntity<StringResponseDto> deleteReservation(@PathVariable Long id,
		@RequestParam(required = false, defaultValue = "single") String type) {
		return ResponseEntity.ok(null);
	}
}
