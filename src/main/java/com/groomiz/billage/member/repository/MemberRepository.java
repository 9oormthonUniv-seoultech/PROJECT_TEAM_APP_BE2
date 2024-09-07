package com.groomiz.billage.member.repository;

import com.groomiz.billage.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByUsername(String username);
    Boolean existsByUsername(String username);
}
