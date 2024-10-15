package com.groomiz.billage.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.groomiz.billage.auth.dto.CustomUserDetails;
import com.groomiz.billage.common.dto.StringResponseDto;
import com.groomiz.billage.global.anotation.ApiErrorExceptionsExample;
import com.groomiz.billage.member.document.UserInfoEditExceptionDocs;
import com.groomiz.billage.member.document.UserInfoExceptionDocs;
import com.groomiz.billage.member.document.UserPasswordExceptionDocs;
import com.groomiz.billage.member.dto.MemberInfoRequest;
import com.groomiz.billage.member.dto.MemberInfoResponse;
import com.groomiz.billage.member.dto.PasswordRequest;
import com.groomiz.billage.member.service.MemberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name = "Member Controller", description = "회원 정보 관리 API")
public class MemberController {

	private final MemberService memberService;

	@GetMapping("/info")
	@Operation(summary = "회원 정보 조회")
	@ApiErrorExceptionsExample(UserInfoExceptionDocs.class)
	public ResponseEntity<MemberInfoResponse> info(@AuthenticationPrincipal CustomUserDetails user) {

		MemberInfoResponse response = memberService.findByStudentNumber(user.getUsername());

		return ResponseEntity.ok(response);
	}

	@PutMapping("/info")
	@Operation(summary = "회원 정보 수정")
	@ApiErrorExceptionsExample(UserInfoEditExceptionDocs.class)
	public ResponseEntity<StringResponseDto> updateInfo(@RequestBody @Valid MemberInfoRequest memberInfoRequest, @AuthenticationPrincipal CustomUserDetails user) {

		memberService.updateMemberInfo(memberInfoRequest.getEmail(), memberInfoRequest.getPhoneNumber(), user.getUsername());

		return ResponseEntity.ok(new StringResponseDto("회원 정보가 성공적으로 수정되었습니다."));
	}

	@DeleteMapping
	@Operation(summary = "회원 탈퇴")
	@ApiErrorExceptionsExample(UserInfoExceptionDocs.class)
	public ResponseEntity<StringResponseDto> delete(@AuthenticationPrincipal CustomUserDetails user) {

		memberService.deleteMember(user.getUsername());

		return ResponseEntity.ok(new StringResponseDto("회원 탈퇴가 성공적으로 처리되었습니다."));
	}

	@PutMapping("/password")
	@Operation(summary = "비밀번호 수정")
	@ApiErrorExceptionsExample(UserPasswordExceptionDocs.class)
	public ResponseEntity<StringResponseDto> updatePassword(@RequestBody @Valid PasswordRequest passwordRequest, @AuthenticationPrincipal CustomUserDetails user) {

		memberService.updatePassword(passwordRequest.getOldPassword(), passwordRequest.getNewPassword(), user.getUsername());

		return ResponseEntity.ok(new StringResponseDto("비밀번호가 성공적으로 수정되었습니다."));
	}

}
