package com.groomiz.billage.member.service;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.groomiz.billage.auth.dto.RegisterRequest;
import com.groomiz.billage.auth.service.RedisService;
import com.groomiz.billage.member.entity.College;
import com.groomiz.billage.member.entity.Major;
import com.groomiz.billage.member.entity.Member;
import com.groomiz.billage.member.entity.Role;
import com.groomiz.billage.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class MemberService {
	private final MemberRepository memberRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	private final RedisService redisService;

	@Value("${spring.data.redis.cache.fcm-ttl}")
	private Long ttl;

	//Todo : 약관동의도 넣어야 함
	public void register(RegisterRequest registerRequest) {
		String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());
		Member member = Member.builder()
			.username(registerRequest.getName())
			.password("{bcrypt}" + encodedPassword)
			.phoneNumber(registerRequest.getPhoneNumber())
			.role(Role.ADMIN)
			.studentNumber(registerRequest.getStudentNumber())
			.isAdmin(true)
			.college(College.fromName(registerRequest.getCollege()))
			.major(Major.fromName(registerRequest.getMajor()))
			.studentEmail(registerRequest.getStudentEmail())
			.build();
		memberRepository.save(member);

	}

	public void saveFCMToken(String token, String studentNumber) {
		String key = "FCM_" + studentNumber;
		redisService.setValues(key, token, Duration.ofMillis(ttl));
	}

	public boolean isExists(String studentNumber) {
		return memberRepository.existsByStudentNumber(studentNumber);
	}
}
