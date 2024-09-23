package com.groomiz.billage;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Duration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.web.servlet.MockMvc;

import com.groomiz.billage.auth.jwt.JwtUtil;

@SpringBootTest
@AutoConfigureMockMvc
public class RefreshTokenRedisTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Autowired
	private JwtUtil jwtUtil;

	private static final String REFRESH_TOKEN_KEY = "test";

	@BeforeEach
	public void setUp() {
		// Redis 클린업 (기존 데이터를 삭제)
		redisTemplate.delete(REFRESH_TOKEN_KEY);
	}

	@Test
	@DisplayName("RefreshToken Redis에 저장 및 검증")
	public void testSaveAndRetrieveRefreshToken() throws Exception {
		// 1. RefreshToken 생성
		String refreshToken = jwtUtil.createJwt("RefreshToken", "test", "ADMIN", 100000L);

		// 2. Redis에 저장
		redisTemplate.opsForValue().set("refreshToken:testUser", refreshToken, Duration.ofDays(1));

		// 3. Redis에서 저장된 RefreshToken을 가져옴
		String storedRefreshToken = redisTemplate.opsForValue().get("refreshToken:testUser");
		assertThat(storedRefreshToken).isEqualTo(refreshToken);
	}

	@Test
	@DisplayName("AccessToken 만료 시 RefreshToken을 통한 재발급")
	public void testAccessTokenExpirationAndRefresh() throws Exception {
		// 1. RefreshToken 생성
		String refreshToken = jwtUtil.createJwt("RefreshToken", "test", "ADMIN", 86400000L); // 1일 만료

		// 2. Redis에 RefreshToken 저장
		redisTemplate.opsForValue().set("test", refreshToken, Duration.ofDays(1));

		// 3. AccessToken 생성 (만료 시간 1초)
		String expiredAccessToken = jwtUtil.createJwt("AccessToken", "test", "ADMIN", 1000L);

		// 4. AccessToken 만료를 위해 2초 대기
		Thread.sleep(2000);

		// 5. 만료된 AccessToken을 사용하여 요청, 401 Unauthorized 기대
		mockMvc.perform(get("/api/admin")
				.header("Authorization", "Bearer " + expiredAccessToken))
			.andExpect(status().isUnauthorized());

		// 6. Redis에서 저장된 RefreshToken 값 확인
		String storedToken = redisTemplate.opsForValue().get("test");
		assertThat(storedToken).isEqualTo(refreshToken);

		// 7. RefreshToken을 사용해 새로운 AccessToken 요청
		mockMvc.perform(post("/api/auth/refresh-token")
				.header("RefreshToken", "Bearer " + refreshToken))
			.andExpect(status().isOk())
			.andExpect(header().exists("Authorization"))
			.andExpect(header().exists("RefreshToken"));
	}
}

