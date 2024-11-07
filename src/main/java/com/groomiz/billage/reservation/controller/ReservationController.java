package com.groomiz.billage.reservation.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.groomiz.billage.auth.document.JwtExceptionDocs;
import com.groomiz.billage.auth.dto.CustomUserDetails;
import com.groomiz.billage.common.dto.StringResponseDto;
import com.groomiz.billage.global.anotation.ApiErrorExceptionsExample;
import com.groomiz.billage.reservation.document.ReservationCancelExceptionDocs;
import com.groomiz.billage.reservation.document.ReservationExceptionDocs;
import com.groomiz.billage.reservation.dto.request.ClassroomReservationRequest;
import com.groomiz.billage.reservation.dto.response.ReservationStatusListResponse;
import com.groomiz.billage.reservation.service.ReservationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/reservations")
@Tag(name = "Reservation Controller", description = "[학생] 예약 관련 API")
public class ReservationController {

	private static final Logger log = LoggerFactory.getLogger(ReservationController.class);
	private final ReservationService reservationService;

	@PostMapping
	@Operation(summary = "강의실 예약")
	@ApiErrorExceptionsExample(ReservationExceptionDocs.class)
	public ResponseEntity<StringResponseDto> reserveClassroom(@RequestBody @Valid ClassroomReservationRequest request
		, @AuthenticationPrincipal CustomUserDetails user) throws FirebaseMessagingException {

		reservationService.reserveClassroom(request, user.getStudentNumber());
		return ResponseEntity.ok(new StringResponseDto("강의실 예약 요청에 성공하였습니다."));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "강의실 예약 취소")
	@ApiErrorExceptionsExample(ReservationCancelExceptionDocs.class)
	public ResponseEntity<StringResponseDto> cancelReservation(
		@Parameter(description = "예약 ID", example = "1")
		@PathVariable("id") Long id,
		@AuthenticationPrincipal CustomUserDetails user
	) {

		reservationService.cancelReservation(id, user.getStudentNumber());
		return ResponseEntity.ok(new StringResponseDto("강의실 예약 취소에 성공하였습니다."));
	}

	@GetMapping
	@Operation(summary = "예약 현황 목록 조회")
	@ApiErrorExceptionsExample(JwtExceptionDocs.class)
	public ResponseEntity<ReservationStatusListResponse> getAllReservationStatus(
		@RequestParam(required = true, defaultValue = "false") boolean isPast,
		@RequestParam(required = false, defaultValue = "1") int page,
		@AuthenticationPrincipal CustomUserDetails user
	) {

		ReservationStatusListResponse all = reservationService.getAllReservationStatus(
			isPast, page,
			user.getStudentNumber());
		return ResponseEntity.ok(all);
	}
}
