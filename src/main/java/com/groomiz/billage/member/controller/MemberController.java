package com.groomiz.billage.member.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.groomiz.billage.member.dto.MemberInfoRequest;
import com.groomiz.billage.member.dto.MemberInfoResponse;
import com.groomiz.billage.member.dto.PasswordRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name = "Member Controller", description = "회원 정보 관리 API")
public class MemberController {

	@GetMapping("/info")
	@Operation(summary = "회원 정보 조회")
	public MemberInfoResponse info() {

		MemberInfoResponse response = null;

		return response;
	}

	@PutMapping("/info")
	@Operation(summary = "회원 정보 수정")
	public String updatePhoneNumber(@RequestBody MemberInfoRequest memberInfoRequest) {
		return "success";
	}

	@DeleteMapping
	@Operation(summary = "회원 탈퇴")
	public String delete() {
		return "success";
	}

	@PutMapping("/password")
	@Operation(summary = "비밀번호 수정")
	public String updatePassword(@RequestBody PasswordRequest passwordRequest) {
		return "success";
	}

}
