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
import com.groomiz.billage.member.service.MemberService;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

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
		RegisterRequest registerRequest = new RegisterRequest("홍길동", "20100000", "password1234!", "010-1234-5678",
			"정보통신대학", "컴퓨터공학과", true, "asdf1234@gmail.com");

		if (!memberService.isExists(registerRequest.getStudentNumber())) {
			memberService.register(registerRequest);
		}

	}

	@Test
	@DisplayName("로그인 성공하였습니다.")
	public void testLoginSuccess() throws Exception {
		LoginRequest loginRequest = new LoginRequest("20100000", "password1234!", "dlG5jjy4SvicNcWvENgF91:APA91bHSERS39latr_mu0jh1A");

		mockMvc.perform(post("/api/v1/users/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginRequest)))
			.andExpect(status().isOk())
			.andExpect(header().exists("Authorization"))
			.andExpect(header().exists("RefreshToken"));
	}

	@Test
	@DisplayName("토큰이 만료 되었습니다.")
	public void testExpiredToken() throws Exception {

		// 만료된 Access Token을 사용한 요청 테스트
		String expiredToken = jwtUtil.createJwt("AccessToken", "20100000", "ADMIN", 1L); // 1ms 유효한 토큰

		// 짧은 시간 동안 토큰 만료를 기다림
		Thread.sleep(1000);

		// AuthException이 발생하는지 확인
		assertThrows(AuthException.class, () -> {
			mockMvc.perform(get("/api/admin")
					.header("Authorization", "Bearer " + expiredToken))
				.andExpect(status().isUnauthorized()); // 만료된 토큰은 401 상태를 기대
		});
	}
	@Test
	@DisplayName("유효하지 않은 토큰입니다.")
	public void testInvalidToken() throws Exception {

		// 유효하지 않은 Access Token을 생성 (JWT 형식이 올바르지 않거나 변조된 토큰)
		String invalidToken = "invalid.token.string"; // 형식이 잘못된 임의의 토큰

		// AuthException이 발생하는지 확인
		assertThrows(AuthException.class, () -> {
			mockMvc.perform(get("/api/admin")
					.header("Authorization", "Bearer " + invalidToken))
				.andExpect(status().isUnauthorized()); // 유효하지 않은 토큰은 401 상태를 기대
		});
	}


}
