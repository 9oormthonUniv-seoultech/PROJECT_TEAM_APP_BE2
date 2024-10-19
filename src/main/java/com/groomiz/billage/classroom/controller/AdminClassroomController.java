package com.groomiz.billage.classroom.controller;

import java.time.LocalDate;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.groomiz.billage.classroom.document.AdminClassroomFilterExceptionDocs;
import com.groomiz.billage.classroom.document.AdminClassroomSearchExceptionDocs;
import com.groomiz.billage.classroom.dto.request.AdminClassroomStatusRequest;
import com.groomiz.billage.classroom.dto.response.AdminClassroomDetailResponse;
import com.groomiz.billage.classroom.dto.response.AdminClassroomStatusResponse;
import com.groomiz.billage.classroom.service.AdminClassroomService;
import com.groomiz.billage.global.anotation.ApiErrorExceptionsExample;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/admin/classrooms")
@Tag(name = "Admin Classroom Controller", description = "[관리자] 강의실 관련 API")
public class AdminClassroomController {

	private final AdminClassroomService adminClassroomService;

	@PostMapping
	@Operation(summary = "강의실 현황 필터링")
	@ApiErrorExceptionsExample(AdminClassroomFilterExceptionDocs.class)
	public ResponseEntity<AdminClassroomStatusResponse> getClassrooms(
		@RequestBody AdminClassroomStatusRequest request) {

		AdminClassroomStatusResponse response = adminClassroomService.findClassroomsByFilter(request);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/info/{id}")
	@Operation(summary = "강의실 상세 조회")
	@ApiErrorExceptionsExample(AdminClassroomSearchExceptionDocs.class)
	public ResponseEntity<AdminClassroomDetailResponse> findByClassroomIdAndDate(
		@Parameter(description = "강의실 ID", example = "1")
		@PathVariable("id") Long id,
		@Parameter(description = "날짜", example = "2024-11-03")
		@RequestParam("date") LocalDate date) {

		AdminClassroomDetailResponse classroom = adminClassroomService.findClassroomByIdAndDate(id, date);
		return ResponseEntity.ok(classroom);
	}

}
