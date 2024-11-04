package com.groomiz.billage.member.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.groomiz.billage.member.dto.response.AdminListResponse;
import com.groomiz.billage.member.dto.response.CollegeListResponse;
import com.groomiz.billage.member.entity.College;
import com.groomiz.billage.member.entity.Member;
import com.groomiz.billage.member.exception.MemberErrorCode;
import com.groomiz.billage.member.exception.MemberException;
import com.groomiz.billage.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UnivService {

	private final MemberRepository memberRepository;

	public List<CollegeListResponse> findAllCollege() {
		// 모든 단과대 가져오기
		List<College> colleges = Arrays.asList(College.values());

		// 각 단과대에 해당하는 학과 리스트로 CollegeListResponse 생성
		return colleges.stream()
			.map(CollegeListResponse::new)
			.collect(Collectors.toList());
	}

	public List<AdminListResponse> findAllAdmin() {
		// 모든 단과대 가져오기
		List<College> colleges = List.of(College.values());

		return colleges.stream()
			.map(this::buildAdminListResponse)
			.collect(Collectors.toList());
	}

	private AdminListResponse buildAdminListResponse(College college) {
		// 단과대에 속하는 어드민 멤버 조회
		List<Member> admins = memberRepository.findByCollegeAndIsAdmin(college, true);

		// 각 멤버를 AdminInfo DTO로 변환
		List<AdminListResponse.AdminInfo> adminInfoList = admins.stream()
			.map(member -> new AdminListResponse.AdminInfo(member.getMajor(), member.getPhoneNumber()))
			.collect(Collectors.toList());

		// 단과대 전화번호를 예시로 설정 (실제 로직에서는 단과대별 전화번호를 가져와야 합니다)
		String collegePhoneNumber = getCollegePhoneNumber(college);

		return new AdminListResponse(college, collegePhoneNumber, adminInfoList);
	}

	private String getCollegePhoneNumber(College college) {
		return college.getName();
	}
}


