package com.groomiz.billage.member.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.groomiz.billage.auth.dto.CustomUserDetails;
import com.groomiz.billage.auth.service.RedisService;
import com.groomiz.billage.common.dto.StringResponseDto;
import com.groomiz.billage.global.anotation.ApiErrorExceptionsExample;
import com.groomiz.billage.member.document.UserInfoEditExceptionDocs;
import com.groomiz.billage.member.document.UserInfoExceptionDocs;
import com.groomiz.billage.member.document.UserPasswordExceptionDocs;
import com.groomiz.billage.member.dto.request.MemberInfoRequest;
import com.groomiz.billage.member.dto.request.PasswordRequest;
import com.groomiz.billage.member.dto.response.MemberInfoResponse;
import com.groomiz.billage.member.service.MemberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name = "Member Controller", description = "회원 정보 관리 API")
public class MemberController {

	private static final Logger log = LoggerFactory.getLogger(MemberController.class);
	private final MemberService memberService;
	private final RedisService redisService;

	@GetMapping("/info")
	@Operation(summary = "회원 정보 조회")
	@ApiErrorExceptionsExample(UserInfoExceptionDocs.class)
	public ResponseEntity<MemberInfoResponse> info() {

		MemberInfoResponse response = null;

		return ResponseEntity.ok(response);
	}

	@PutMapping("/info")
	@Operation(summary = "회원 정보 수정")
	@ApiErrorExceptionsExample(UserInfoEditExceptionDocs.class)
	public ResponseEntity<StringResponseDto> updatePhoneNumber(@RequestBody MemberInfoRequest memberInfoRequest) {
		return ResponseEntity.ok(new StringResponseDto("success"));
	}

	@DeleteMapping
	@Operation(summary = "회원 탈퇴")
	@ApiErrorExceptionsExample(UserInfoExceptionDocs.class)
	public ResponseEntity<StringResponseDto> delete(
		@AuthenticationPrincipal CustomUserDetails user) {

		// FCM 토큰 삭제
		redisService.deleteValues("FCM_" + user);
		return ResponseEntity.ok(new StringResponseDto("success"));
	}

	@PutMapping("/password")
	@Operation(summary = "비밀번호 수정")
	@ApiErrorExceptionsExample(UserPasswordExceptionDocs.class)
	public ResponseEntity<StringResponseDto> updatePassword(@RequestBody PasswordRequest passwordRequest) {
		return ResponseEntity.ok(new StringResponseDto("success"));
	}

	@PostMapping("/fcmtoken")
	@Operation(summary = "FCM 토큰 저장")
	public ResponseEntity<StringResponseDto> saveFcmToken(
		@Schema(description = "FCM 토큰", example = "dlG5jjy4SvicNcWvENgF91:APA91bHSERS39latr_mu0jh1A")
		@RequestHeader("FCM-Token") String token,
		@AuthenticationPrincipal CustomUserDetails user) {

		memberService.saveFCMToken(token, user.getStudentNumber());
		return ResponseEntity.ok(new StringResponseDto("FCM 토큰 저장에 성공하였습니다."));
	}

}
