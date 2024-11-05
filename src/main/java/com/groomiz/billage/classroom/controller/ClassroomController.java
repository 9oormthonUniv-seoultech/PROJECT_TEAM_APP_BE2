package com.groomiz.billage.classroom.controller;

import java.time.LocalDate;
import java.util.List;

import com.groomiz.billage.classroom.dto.response.ClassroomImageResponse;
import com.groomiz.billage.classroom.service.ClassroomImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import com.groomiz.billage.classroom.dto.request.ClassroomListRequest;
import com.groomiz.billage.classroom.dto.response.ClassroomDetailResponse;
import com.groomiz.billage.classroom.dto.response.ClassroomListResponse;
import com.groomiz.billage.classroom.service.ClassroomService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/univ/classroom")
@Tag(name = "Classroom Controller", description = "[학생] 강의실 관련 API")
public class ClassroomController {

	private final ClassroomImageService classroomImageService;
	private final ClassroomService classroomService;

	@PostMapping
	@Operation(summary = "강의실 목록 조회")
	public ResponseEntity<List<ClassroomListResponse>> findAll(@Valid @RequestBody ClassroomListRequest request) {

		List<ClassroomListResponse> response = classroomService.findAllClassroom(request);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/info/{id}")
	@Operation(summary = "강의실 상세 조회")
	public ResponseEntity<ClassroomDetailResponse> findByClassroomIdAndDate(
		@Parameter(description = "강의실 ID", example = "1")
		@PathVariable("id") Long id,
		@Parameter(description = "날짜", example = "2024-11-03")
		@RequestParam("date") LocalDate date) {

		ClassroomDetailResponse classroom = classroomService.findClassroomByIdAndDate(id, date);
		return ResponseEntity.ok(classroom);
	}

	@PostMapping("/{classroomId}/images")
	@Operation(summary = "강의실 이미지 업로드")
	public ResponseEntity<ClassroomImageResponse> uploadClassroomImage(
			@Parameter(description = "강의실 ID", example = "1")
			@PathVariable("classroomId") Long classroomId,
			@Parameter(description = "업로드할 이미지 파일")
			@RequestParam("image") MultipartFile image) {

		try {
			ClassroomImageResponse response = classroomImageService.uploadClassroomImage(classroomId, image);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ClassroomImageResponse(null, classroomId, "이미지 업로드 중 오류가 발생했습니다."));
		}
	}
}
