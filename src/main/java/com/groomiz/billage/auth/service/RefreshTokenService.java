package com.groomiz.billage.auth.service;

import java.time.Duration;

import org.springframework.stereotype.Service;

import com.groomiz.billage.auth.dto.LoginResponse;
import com.groomiz.billage.auth.exception.AuthErrorCode;
import com.groomiz.billage.auth.exception.AuthException;
import com.groomiz.billage.auth.jwt.JwtUtil;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

	private final JwtUtil jwtUtil;
	private final RedisService redisService;

	public LoginResponse reissue(String refreshToken) {

		// RefreshToken이 존재하지 않거나 형식이 올바르지 않은 경우 예외 처리
		if (refreshToken == null || !refreshToken.startsWith("Bearer ")) {
			throw new AuthException(AuthErrorCode.TOKEN_NOT_FOUND);
		}

		// Bearer 부분 제거
		refreshToken = refreshToken.substring(7);

		// RefreshToken이 만료되었는지 확인
		try {
			jwtUtil.isExpired(refreshToken);
		} catch (ExpiredJwtException e) {
			throw new AuthException(AuthErrorCode.TOKEN_EXPIRED);
		}

		String studentNumber = jwtUtil.getStudentNumber(refreshToken);
		String storedToken = redisService.getValues(studentNumber);

		// Redis에 저장된 토큰과 비교하여 유효성 검증
		if (!refreshToken.equals(storedToken)) {
			throw new AuthException(AuthErrorCode.INVALID_TOKEN);
		}

		// 새로운 AccessToken 및 RefreshToken 생성
		String role = jwtUtil.getRole(refreshToken);
		String newAccessToken = jwtUtil.createJwt("AccessToken", studentNumber, role, 600000L); // 10분
		String newRefreshToken = jwtUtil.createJwt("RefreshToken", studentNumber, role, 86400000L); // 1일

		// 기존 토큰 삭제 및 새 토큰 저장
		redisService.deleteValues(studentNumber);
		redisService.setValues(studentNumber, newRefreshToken, Duration.ofDays(1));

		// 새로운 AccessToken과 RefreshToken을 담은 LoginResponse 반환
		return new LoginResponse("Bearer " + newAccessToken, "Bearer " + newRefreshToken);
	}
}
