package com.groomiz.billage.reservation.controller;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.groomiz.billage.auth.document.JwtExceptionDocs;
import com.groomiz.billage.auth.dto.CustomUserDetails;
import com.groomiz.billage.common.dto.StringResponseDto;
import com.groomiz.billage.global.anotation.ApiErrorExceptionsExample;
import com.groomiz.billage.member.exception.MemberErrorCode;
import com.groomiz.billage.member.exception.MemberException;
import com.groomiz.billage.reservation.document.ReservationCancelExceptionDocs;
import com.groomiz.billage.reservation.document.ReservationExceptionDocs;
import com.groomiz.billage.reservation.dto.request.ClassroomReservationRequest;
import com.groomiz.billage.reservation.dto.response.ReservationStatusListResponse;
import com.groomiz.billage.reservation.entity.ReservationPurpose;
import com.groomiz.billage.reservation.exception.ReservationErrorCode;
import com.groomiz.billage.reservation.exception.ReservationException;
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
		, @AuthenticationPrincipal CustomUserDetails user) {

		// 예약 인원 음수 예외
		if (request.getHeadcount() <= 0) {
			throw new ReservationException(ReservationErrorCode.NEGATIVE_PARTICIPANTS);
		}

		reservationService.reserveClassroom(request, user.getStudentNumber());
		return ResponseEntity.ok(new StringResponseDto("강의실 예약 요청에 성공하였습니다."));
	}

	@PostMapping("/{id}")
	@Operation(summary = "강의실 예약 취소")
	@ApiErrorExceptionsExample(ReservationCancelExceptionDocs.class)
	public ResponseEntity<StringResponseDto> cancleReservation(
		@Parameter(description = "예약 ID", example = "1")
		@PathVariable("id") Long id,
		@AuthenticationPrincipal CustomUserDetails user
	) {

		reservationService.cancleReservation(id, user.getStudentNumber());
		return ResponseEntity.ok(new StringResponseDto("강의실 예약 취소에 성공하였습니다."));
	}

	@GetMapping
	@Operation(summary = "예약 현황 목록 조회")
	@ApiErrorExceptionsExample(JwtExceptionDocs.class)
	public ResponseEntity<List<ReservationStatusListResponse>> getAllReservationStatus() {

		List<ReservationStatusListResponse> response = null;
		return ResponseEntity.ok(response);
	}
}
