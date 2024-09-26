package com.groomiz.billage.member.entity;

import static org.assertj.core.api.AssertionsForInterfaceTypes.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class MajorTest {

	@Test
	public void 단과대에_소속된_학과_조회_성공() throws Exception {
		//given
		College college = College.ENGINEER;

		//when
		List<Major> majors = Major.getMajorsByCollege(college);
		log.info("majors: {}", majors);

		//then
		assertThat(majors).contains(Major.MSD);
		assertThat(majors).isNotEmpty();
	}
}