package com.groomiz.billage.member.service;

import static com.groomiz.billage.member.exception.MemberErrorCode.*;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.groomiz.billage.auth.dto.RegisterRequest;
import com.groomiz.billage.member.dto.MemberInfoResponse;
import com.groomiz.billage.member.entity.College;
import com.groomiz.billage.member.entity.Major;
import com.groomiz.billage.member.entity.Member;
import com.groomiz.billage.member.entity.Role;
import com.groomiz.billage.member.exception.MemberException;
import com.groomiz.billage.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MemberService {
	private final MemberRepository memberRepository;
	private final BCryptPasswordEncoder passwordEncoder;

	public void register(RegisterRequest registerRequest) {
		String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());

		if (isExists(registerRequest.getStudentNumber())) {
			throw new MemberException(STUDENT_ID_ALREADY_REGISTERED);
		}

		Member member = Member.builder()
			.username(registerRequest.getName())
			.password("{bcrypt}" + encodedPassword)
			.phoneNumber(registerRequest.getPhoneNumber())
			.role(Role.ADMIN)
			.studentNumber(registerRequest.getStudentNumber())
			.isAdmin(true)
			.agreedToTerms(registerRequest.isAgreedToTerms())
			.college(College.fromName(registerRequest.getCollege()))
			.major(Major.fromNameAndCollege(registerRequest.getMajor(), registerRequest.getCollege()))
			.studentEmail(registerRequest.getStudentEmail())
			.build();
		memberRepository.save(member);
	}

	public MemberInfoResponse findByStudentNumber(String studentNumber) {

		Member byStudentNumber = memberRepository.findByStudentNumber(studentNumber)
			.orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

		//Todo : 예약 횟수도 넣어야 함
		return MemberInfoResponse.builder()
			.studentNumber(byStudentNumber.getStudentNumber())
			.name(byStudentNumber.getUsername())
			.phoneNumber(byStudentNumber.getPhoneNumber())
			.college(byStudentNumber.getCollege())
			.major(byStudentNumber.getMajor())
			.email(byStudentNumber.getStudentEmail())
			.reservationCount(0)
			.build();
	}

	public void updateMemberInfo(String email, String phoneNumber, String studentNumber) {
		Member member = memberRepository.findByStudentNumber(studentNumber)
			.orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

		member.changePhoneNumber(phoneNumber); // 명시적인 메서드 사용
		member.changeEmail(email);

		memberRepository.save(member);
	}

	public void deleteMember(String studentNumber) {
		// 학번으로 회원을 조회하고 존재하지 않으면 예외를 던짐
		Member member = memberRepository.findByStudentNumber(studentNumber)
			.orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

		// 회원 삭제
		memberRepository.delete(member);
	}

	//비밀번호 업데이트
	public void updatePassword(String oldPassword, String newPassword, String studentNumber) {
		Member member = memberRepository.findByStudentNumber(studentNumber)
			.orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

		// 기존 비밀번호가 일치하지 않으면 예외를 던짐
		String storedPassword = member.getPassword();
		if (storedPassword.startsWith("{bcrypt}")) {
			storedPassword = storedPassword.substring(8);  // {bcrypt} 접두사 제거
		}

		if (!passwordEncoder.matches(oldPassword, storedPassword)) {
			throw new MemberException(INVALID_OLD_PASSWORD);  // 기존 비밀번호 불일치 시 예외 발생
		}

		if(oldPassword.equals(newPassword)) {
			throw new MemberException(PASSWORD_SAME_AS_OLD);
		}

		// 저장하는 로직
		String encodedPassword = passwordEncoder.encode(newPassword);
		member.changePassword("{bcrypt}" + encodedPassword);

		memberRepository.save(member);
	}


	public boolean isExists(String studentNumber) {
		return memberRepository.existsByStudentNumber(studentNumber)
			.orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
	}
}
