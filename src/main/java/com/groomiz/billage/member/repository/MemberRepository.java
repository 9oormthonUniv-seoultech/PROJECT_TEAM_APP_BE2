package com.groomiz.billage.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.groomiz.billage.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

	Member findByStudentNumber(Integer studentNumber);

	Boolean existsByStudentNumber(Integer studentNumber);
}
