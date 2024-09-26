package com.groomiz.billage.reservation.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.groomiz.billage.reservation.dto.request.ClassroomReservationRequest;
import com.groomiz.billage.reservation.dto.response.ReservationStatusListResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/reservations")
@Tag(name = "Reservation Controller", description = "[학생] 예약 관련 API")
public class ReservationController {

	@PostMapping
	@Operation(summary = "강의실 예약")
	public String reserveClassroom(@RequestBody ClassroomReservationRequest request) {

		return "success";
	}

	@DeleteMapping
	@Operation(summary = "강의실 예약 취소")
	public String cancelClassroomReservation(
		@Parameter(description = "예약 ID", example = "1")
		@PathVariable("id") Long id) {

		return "success";
	}

	@GetMapping
	@Operation(summary = "예약 현황 목록 조회")
	public List<ReservationStatusListResponse> getAllReservationStatus() {

		List<ReservationStatusListResponse> response = null;
		return response;
	}
}
