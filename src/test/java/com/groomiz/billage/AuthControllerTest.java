package com.groomiz.billage;

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
			"정보통신대학", "컴퓨터공학과", "asdf1234@gmail.com");

		if (!memberService.isExists(registerRequest.getStudentNumber())) {
			memberService.register(registerRequest);
		}

	}

	@Test
	@DisplayName("로그인 성공하였습니다.")
	public void testLoginSuccess() throws Exception {
		LoginRequest loginRequest = new LoginRequest("20100000", "password1234!");

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
		String expiredToken = jwtUtil.createJwt("AccessToken", "20100000", "ADMIN", 1L);

		Thread.sleep(1000);

		mockMvc.perform(get("/api/admin")
				.header("Authorization", "Bearer " + expiredToken))
			.andExpect(status().isUnauthorized());
	}

}
