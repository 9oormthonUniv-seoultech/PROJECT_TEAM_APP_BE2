package com.groomiz.billage.auth;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.groomiz.billage.auth.dto.LoginRequest;
import com.groomiz.billage.auth.dto.RegisterRequest;
import com.groomiz.billage.auth.exception.AuthException;
import com.groomiz.billage.auth.jwt.JwtUtil;
import com.groomiz.billage.member.exception.MemberException;
import com.groomiz.billage.member.service.MemberService;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MemberService memberService;

	@Autowired
	private JwtUtil jwtUtil;

	@BeforeEach
	public void setUp() {
		// 회원가입을 위한 테스트 데이터를 미리 설정
		RegisterRequest registerRequest = new RegisterRequest("홍길동", "20100000", "password1234!", "010-1234-5678",
			"정보통신대학", "컴퓨터공학과", true, "asdf1234@gmail.com");

		if (!memberService.isExists(registerRequest.getStudentNumber())) {
			memberService.register(registerRequest);
		}
	}

	@Test
	@DisplayName("로그인 성공")
	public void testLoginSuccess() throws Exception {
		// 로그인 요청 생성
		LoginRequest loginRequest = new LoginRequest("20100000", "password1234!", "dlG5jjy4SvicNcWvENgF91:APA91bHSERS39latr_mu0jh1A");

		mockMvc.perform(post("/api/v1/users/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginRequest)))
			.andExpect(status().isOk())
			.andExpect(header().exists("Authorization"))
			.andExpect(header().exists("RefreshToken"));
	}

	@Test
	@DisplayName("로그인 실패 - 잘못된 비밀번호")
	public void testLoginFailure() throws Exception {
		// 잘못된 비밀번호로 로그인 요청
		LoginRequest loginRequest = new LoginRequest("20100000", "wrongpassword!", "dlG5jjy4SvicNcWvENgF91:APA91bHSERS39latr_mu0jh1A");

		// AuthException 발생하는지 확인
		mockMvc.perform(post("/api/v1/users/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginRequest)))
			.andExpect(status().isBadRequest());

	}

	@Test
	@DisplayName("로그인 실패 - 잘못된 아이디")
	public void testLoginFailureWithInvalidUserId() throws Exception {
		// 존재하지 않는 아이디로 로그인 요청
		LoginRequest loginRequest = new LoginRequest("99999999", "password1234!", "dlG5jjy4SvicNcWvENgF91:APA91bHSERS39latr_mu0jh1A");

		// 아이디가 틀렸을 때 AuthException 발생하는지 확인
		mockMvc.perform(post("/api/v1/users/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginRequest)))
			.andExpect(status().isBadRequest()); // 혹은 UNAUTHORIZED로 설정
	}

	@Test
	@DisplayName("회원가입 성공")
	public void testRegisterSuccess() throws Exception {
		RegisterRequest registerRequest = new RegisterRequest("이순신", "20190000", "newpassword!", "010-5678-1234",
			"정보통신대학", "전자공학과", true, "lee1234@gmail.com");

		mockMvc.perform(post("/api/v1/users/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(registerRequest)))
			.andExpect(status().isOk())
			.andExpect(content().string("회원가입이 완료되었습니다."));
	}

	@Test
	@DisplayName("회원 로그아웃 성공")
	public void testLogoutSuccess() throws Exception {
		mockMvc.perform(post("/api/v1/users/logout"))
			.andExpect(status().isOk())
			.andExpect(content().string("로그아웃 성공하였습니다."));
	}

}
