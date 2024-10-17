package com.groomiz.billage.auth.jwt;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.groomiz.billage.auth.config.SecurityProperties;
import com.groomiz.billage.auth.dto.CustomUserDetails;
import com.groomiz.billage.auth.exception.AuthErrorCode;
import com.groomiz.billage.auth.exception.AuthException;
import com.groomiz.billage.global.dto.ErrorResponse;
import com.groomiz.billage.member.entity.Member;
import com.groomiz.billage.member.entity.Role;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final SecurityProperties securityProperties;
	private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule())
		.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // ObjectMapper 재사용

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		// 헤더에서 Authorization에 담긴 토큰을 꺼냄
		String authorizationHeader = request.getHeader("Authorization");

		// 토큰이 없다면 예외 처리
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		try {
			// Bearer 접두사를 제거하고 순수 토큰 값만 추출
			String accessToken = authorizationHeader.substring(7);

			// 토큰 만료 여부 확인 및 만료된 경우 예외 처리
			jwtUtil.isExpired(accessToken);

			// 토큰이 AccessToken인지 확인 (발급시 페이로드에 명시)
			String category = jwtUtil.getCategory(accessToken);

			if (category == null || !category.equals("AccessToken")) {
				throw new AuthException(AuthErrorCode.INVALID_TOKEN);
			}

			// username, role 값을 획득
			String studentNumber = jwtUtil.getStudentNumber(accessToken);
			String role = jwtUtil.getRole(accessToken);

			if (studentNumber == null || role == null) {
				log.error("Invalid claims in token: studentNumber={}, role={}", studentNumber, role); // 추가된 로그
				sendErrorResponse(request, response, AuthErrorCode.INVALID_TOKEN);
				return;
			}

			// SecurityContext에 인증 정보 설정
			Member member = Member.builder()
				.studentNumber(studentNumber)
				.role(Role.valueOf(role))
				.build();

			CustomUserDetails customUserDetails = new CustomUserDetails(member);

			Authentication authToken = new UsernamePasswordAuthenticationToken(
				customUserDetails, null, customUserDetails.getAuthorities());

			SecurityContextHolder.getContext().setAuthentication(authToken);

			// 필터 체인을 계속 실행
			filterChain.doFilter(request, response);

		} catch (ExpiredJwtException e) {
			log.error("Expired JWT token", e); // 추가된 로그
			sendErrorResponse(request, response, AuthErrorCode.TOKEN_EXPIRED);
		} catch (AuthException e) {
			log.error("JWT Authentication failed", e); // 추가된 로그
			sendErrorResponse(request, response, (AuthErrorCode) e.getErrorCode());  // 예외에 담긴 정확한 에러 코드 사용
		} catch (Exception e) {
			log.error("Authentication failed", e); // 추가된 로그
			sendErrorResponse(request, response, AuthErrorCode.AUTHENTICATION_FAIL);
		}
	}


	private void sendErrorResponse(HttpServletRequest request, HttpServletResponse response, AuthErrorCode errorCode) throws IOException {
		response.setStatus(errorCode.getStatus());
		response.setContentType("application/json;charset=UTF-8");
		String path = request.getRequestURI();  // 요청 경로 가져오기
		// ErrorResponse 생성
		ErrorResponse errorResponse = new ErrorResponse(
			errorCode.getStatus(),  // 에러 코드의 상태 값
			errorCode.getCode(),    // 에러 코드
			errorCode.getReason(),  // 에러 메시지
			path                    // 요청 경로
		);

		// 응답에 JSON 쓰기
		String jsonResponse = objectMapper.writeValueAsString(errorResponse);
		PrintWriter writer = response.getWriter();
		writer.print(jsonResponse);
		writer.flush();
	}

}