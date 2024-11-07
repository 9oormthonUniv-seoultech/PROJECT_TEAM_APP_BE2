package com.groomiz.billage.auth.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.groomiz.billage.auth.document.CertificateEmailExceptionDocs;
import com.groomiz.billage.auth.document.LoginExceptionDocs;
import com.groomiz.billage.auth.document.RegisterExceptionDocs;
import com.groomiz.billage.auth.document.StudentNumberExcptionDocs;
import com.groomiz.billage.auth.document.VerifyEmailException;
import com.groomiz.billage.auth.dto.LoginRequest;
import com.groomiz.billage.auth.dto.LoginResponse;
import com.groomiz.billage.auth.dto.RegisterRequest;
import com.groomiz.billage.auth.service.AuthService;
import com.groomiz.billage.auth.service.UnivCertService;
import com.groomiz.billage.common.dto.StringResponseDto;
import com.groomiz.billage.global.anotation.ApiErrorExceptionsExample;
import com.groomiz.billage.global.dto.ErrorReason;
import com.groomiz.billage.global.dto.ErrorResponse;
import com.groomiz.billage.member.exception.MemberErrorCode;
import com.groomiz.billage.member.service.MemberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User Controller", description = "회원 인증/인가 관련 API")
public class UserController {

	private final AuthService authService;
	private final MemberService memberService;
	private final UnivCertService univCertService;

	@PostMapping("/login")
	@Operation(summary = "회원 로그인")
	@ApiErrorExceptionsExample(LoginExceptionDocs.class)
	public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginRequest, HttpServletResponse response) {

		LoginResponse login = authService.login(loginRequest, response);

		return ResponseEntity.ok(login);
	}

	@PostMapping("/logout")
	@Operation(summary = "회원 로그아웃")
	public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
		try {
			authService.logout(request, response);
			return ResponseEntity.ok(new StringResponseDto("로그아웃 성공하였습니다."));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Logout failed: " + e.getMessage());
		}
	}

	@PostMapping("/register")
	@Operation(summary = "회원 가입")
	@ApiErrorExceptionsExample(RegisterExceptionDocs.class)
	public ResponseEntity<?> join(@RequestBody @Valid RegisterRequest registerRequest) {

		memberService.register(registerRequest);

		return ResponseEntity.ok(new StringResponseDto("회원가입이 완료되었습니다."));
	}

	@GetMapping("/check-student-number")
	@Operation(summary = "학번 중복 확인")
	@ApiErrorExceptionsExample(StudentNumberExcptionDocs.class)
	public ResponseEntity<?> checkStudentNumber(
		@Parameter(description = "학번", example = "20100000") @NotNull @RequestParam String studentNumber, HttpServletRequest request) {

		authService.checkStudentNumberExists(studentNumber);

		return ResponseEntity.ok(new StringResponseDto("사용 가능한 학번입니다."));
	}

	@PostMapping("/certificate")
	@Operation(summary = "이메일 인증 요청")
	@ApiErrorExceptionsExample(CertificateEmailExceptionDocs.class)
	public ResponseEntity<?> certificate(
		@Parameter(description = "이메일", example = "menten4859@seoultech.ac.kr") @RequestParam String email) {

		Map<?, ?> certificated = univCertService.certificate(email);

		return ResponseEntity.ok(certificated);
	}

	@PostMapping("/verify")
	@Operation(summary = "이메일 인증 코드 검증")
	@ApiErrorExceptionsExample(VerifyEmailException.class)
	public ResponseEntity<?> verify(
		@Parameter(description = "이메일", example = "menten4859@seoultech.ac.kr") @RequestParam String email,
		@Parameter(description = "인증 코드", example = "123456") @RequestParam int codeNumber) {

		Map<?, ?> verify = univCertService.verify(email, codeNumber);
		return ResponseEntity.ok(verify);
	}

	@PostMapping("/clear-email")
	@Operation(summary = "[개발용] 이메일 인증 정보 삭제")
	public ResponseEntity<?> clearEmail() {

		Map<?, ?> responseMap = univCertService.clearEmailList();
		return ResponseEntity.ok(responseMap);
	}

}
