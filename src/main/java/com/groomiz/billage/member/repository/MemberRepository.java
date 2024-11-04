package com.groomiz.billage.member.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.groomiz.billage.member.entity.College;
import com.groomiz.billage.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

	Optional<Member> findByStudentNumber(String studentNumber);

	Optional<Boolean> existsByStudentNumber(String studentNumber);

	List<Member> findByCollegeAndIsAdmin(College college, boolean isAdmin);

}
