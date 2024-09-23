package com.groomiz.billage.member.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.groomiz.billage.member.dto.JoinRequest;
import com.groomiz.billage.member.entity.Member;
import com.groomiz.billage.member.entity.Role;
import com.groomiz.billage.member.repository.MemberRepository;

@Service
public class MemberService {
	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;

	public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
		this.memberRepository = memberRepository;
		this.passwordEncoder = passwordEncoder;
	}

	//TODO: Join할 때 phone number도 받아야 함
	public void register(JoinRequest joinRequest) {
		String encodedPassword = passwordEncoder.encode(joinRequest.getPassword());
		Member member = Member.builder()
			.username(joinRequest.getUsername())
			.password(encodedPassword)
			.role(Role.ADMIN)
			.build();
		memberRepository.save(member);

	}

	public boolean isExists(String username) {
		return memberRepository.existsByUsername(username);
	}
}
