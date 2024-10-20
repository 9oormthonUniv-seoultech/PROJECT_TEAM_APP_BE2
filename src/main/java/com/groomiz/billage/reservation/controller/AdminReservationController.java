package com.groomiz.billage.reservation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.groomiz.billage.auth.dto.CustomUserDetails;
import com.groomiz.billage.classroom.dto.response.AdminReservationReasonResponse;
import com.groomiz.billage.common.dto.StringResponseDto;
import com.groomiz.billage.global.anotation.ApiErrorExceptionsExample;
import com.groomiz.billage.reservation.document.AdminApproveRejectExceptionDocs;
import com.groomiz.billage.reservation.document.AdminReservationDeleteExceptionDocs;
import com.groomiz.billage.reservation.document.AdminReservationExceptionDocs;
import com.groomiz.billage.reservation.document.AdminSearchExceptionDocs;
import com.groomiz.billage.reservation.document.AdminbyStatusExceptionDocs;
import com.groomiz.billage.reservation.dto.request.AdminRejectionRequest;
import com.groomiz.billage.reservation.dto.request.AdminReservationRequest;
import com.groomiz.billage.reservation.dto.response.AdminReservationResponse;
import com.groomiz.billage.reservation.dto.response.AdminReservationStatusListResponse;
import com.groomiz.billage.reservation.entity.ReservationStatusType;
import com.groomiz.billage.reservation.service.AdminReservationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/admin/reservations")
@Tag(name = "Admin Reservation Controller", description = "[관리자] 예약 관련 API")
@RequiredArgsConstructor
public class AdminReservationController {

	private final AdminReservationService adminReservationService;

	@GetMapping
	@Operation(summary = "예약 상태별 리스트 조회")
	@ApiErrorExceptionsExample(AdminbyStatusExceptionDocs.class)
	public ResponseEntity<AdminReservationStatusListResponse> getReservationsByStatus(
		@Parameter(description = "예약 상태 (PENDING/APPROVED/REJECTED)", example = "APPROVED", required = true)
		@RequestParam(required = true) String status,
		@AuthenticationPrincipal CustomUserDetails user) {

		ReservationStatusType type = ReservationStatusType.valueOf(status.toUpperCase());

		AdminReservationStatusListResponse reservations = adminReservationService.getReservationByStatus(type,
			user.getStudentNumber());
		return ResponseEntity.ok(reservations);
	}

	@PostMapping("/{id}/approve")
	@Operation(summary = "예약 승인")
	@ApiErrorExceptionsExample(AdminApproveRejectExceptionDocs.class)
	public ResponseEntity<StringResponseDto> approveReservation(
		@PathVariable Long id,
		@AuthenticationPrincipal CustomUserDetails user) throws FirebaseMessagingException {

		adminReservationService.approveReservation(id, user.getStudentNumber());

		return ResponseEntity.ok(new StringResponseDto("예약 승인 완료되었습니다."));
	}

	@PostMapping("/{id}/reject")
	@Operation(summary = "예약 거절")
	@ApiErrorExceptionsExample(AdminApproveRejectExceptionDocs.class)
	public ResponseEntity<AdminReservationReasonResponse> rejectReservation(@PathVariable Long id,
		@RequestBody AdminRejectionRequest request,
		@AuthenticationPrincipal CustomUserDetails user) throws FirebaseMessagingException {

		adminReservationService.rejectReservation(id, request.getReason(), user.getStudentNumber());

		AdminReservationReasonResponse response = new AdminReservationReasonResponse(
			"예약 거절 완료되었습니다.", request.getReason());
		return ResponseEntity.ok(response);
	}

	@PostMapping
	@Operation(summary = "강의실 예약(일반/기간/반복)")
	@ApiErrorExceptionsExample(AdminReservationExceptionDocs.class)
	public ResponseEntity<StringResponseDto> createReservation(
		@RequestBody AdminReservationRequest request,
		@AuthenticationPrincipal CustomUserDetails user) {

		adminReservationService.reserveClassroom(request, user.getStudentNumber());
		return ResponseEntity.ok(new StringResponseDto("예약 완료 하였습니다."));
	}

	@GetMapping("/{reservationId}")
	@Operation(summary = "예약 상세 조회")
	@ApiErrorExceptionsExample(AdminSearchExceptionDocs.class)
	public ResponseEntity<AdminReservationResponse> getReservation(
		@Parameter(description = "예약 ID", example = "1")
		@PathVariable Long reservationId) {

		AdminReservationResponse reservation = adminReservationService.getReservation(reservationId);
		return ResponseEntity.ok(reservation);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "예약 취소", description = "관리자 일반/기간/반복 예약을 취소합니다.")
	@ApiErrorExceptionsExample(AdminReservationDeleteExceptionDocs.class)
	public ResponseEntity<StringResponseDto> cancelReservation(
		@Parameter(description = "예약 ID", example = "1")
		@PathVariable("id") Long id,
		@Parameter(description = "일괄 삭제 여부", example = "0")
		@RequestParam(required = false, defaultValue = "0") boolean isDeleteAll,
		@AuthenticationPrincipal CustomUserDetails user) {

		adminReservationService.cancelReservation(id, isDeleteAll, user.getStudentNumber());
		return ResponseEntity.ok(new StringResponseDto("예약 취소 완료되었습니다."));
	}

	@DeleteMapping("/{id}/student")
	@Operation(summary = "예약 강제 취소", description = "학생 예약을 강제 취소합니다.")
	@ApiErrorExceptionsExample(AdminReservationDeleteExceptionDocs.class)
	public ResponseEntity<StringResponseDto> cancleStudentReservation(
		@Parameter(description = "예약 ID", example = "1")
		@PathVariable("id") Long id,
		@AuthenticationPrincipal CustomUserDetails user) {

		adminReservationService.cancelStudentReservation(id, user.getStudentNumber());
		return ResponseEntity.ok(new StringResponseDto("학생 예약 강제 취소 완료되었습니다."));
	}
}
