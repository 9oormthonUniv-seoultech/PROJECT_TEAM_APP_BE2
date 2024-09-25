package com.groomiz.billage.auth.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.groomiz.billage.auth.dto.CustomUserDetails;
import com.groomiz.billage.member.entity.Member;
import com.groomiz.billage.member.repository.MemberRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final MemberRepository memberRepository;

	public CustomUserDetailsService(MemberRepository memberRepository) {

		this.memberRepository = memberRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String studentNumber) throws UsernameNotFoundException {

		Member member = memberRepository.findByStudentNumber(Integer.parseInt(studentNumber));

		if (member != null) {

			return new CustomUserDetails(member);
		}

		return null;
	}
}

