package com.groomiz.billage.classroom.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.groomiz.billage.classroom.document.AdminClassroomFilterExceptionDocs;
import com.groomiz.billage.classroom.dto.request.AdminClassroomStatusRequest;
import com.groomiz.billage.classroom.dto.response.AdminClassroomStatusResponse;
import com.groomiz.billage.classroom.dto.response.ClassroomDetailResponse;
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
	@PostMapping
	@Operation(summary = "강의실 현황 필터링")
	@ApiErrorExceptionsExample(AdminClassroomFilterExceptionDocs.class)
	public ResponseEntity<List<AdminClassroomStatusResponse>> getClassrooms(
		@RequestBody AdminClassroomStatusRequest request) {

		ResponseEntity<List<AdminClassroomStatusResponse>> response = null;
		return response;
	}

	@GetMapping("/info")
	@Operation(summary = "강의실 상세 조회")
	@ApiErrorExceptionsExample(AdminClassroomFilterExceptionDocs.class)
	public ResponseEntity<ClassroomDetailResponse> findByClassroomId(
		@Parameter(description = "강의실 ID", example = "1")
		@RequestParam("id") Long id) {

		ResponseEntity<ClassroomDetailResponse> response = null;
		return response;
	}

}
