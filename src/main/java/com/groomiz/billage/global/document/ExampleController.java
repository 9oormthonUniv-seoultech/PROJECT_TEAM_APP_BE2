package com.groomiz.billage.global.document;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.groomiz.billage.auth.exception.AuthErrorCode;
import com.groomiz.billage.building.exception.BuildingErrorCode;
import com.groomiz.billage.classroom.exception.ClassroomErrorCode;
import com.groomiz.billage.global.anotation.ApiErrorCodeExample;
import com.groomiz.billage.global.exception.GlobalErrorCode;
import com.groomiz.billage.reservation.exception.ReservationErrorCode;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/example")
@RequiredArgsConstructor
@Tag(name = "Exception Document", description = "예제 에러코드 문서화")
public class ExampleController {
	@GetMapping("/global")
	@ApiErrorCodeExample(GlobalErrorCode.class)
	@Operation(summary = "글로벌 (aop, 서버 내부 오류등)  관련 에러 코드 나열")
	public void example() {
	}

	@GetMapping("/building")
	@ApiErrorCodeExample(BuildingErrorCode.class)
	@Operation(summary = "건물 관련 에러 코드 나열")
	public void example2() {
	}

	@GetMapping("/auth")
	@ApiErrorCodeExample(AuthErrorCode.class)
	@Operation(summary = "인증 관련 에러 코드 나열")
	public void example3() {
	}

	@GetMapping("/classroom")
	@ApiErrorCodeExample(ClassroomErrorCode.class)
	@Operation(summary = "강의실 관련 에러 코드 나열")
	public void example4() {
	}

	@GetMapping("/member")
	@ApiErrorCodeExample(ClassroomErrorCode.class)
	@Operation(summary = "회원 관련 에러 코드 나열")
	public void example5() {
	}

	@GetMapping("/reservation")
	@ApiErrorCodeExample(ReservationErrorCode.class)
	@Operation(summary = "예약 관련 에러 코드 나열")
	public void example6() {
	}

}
