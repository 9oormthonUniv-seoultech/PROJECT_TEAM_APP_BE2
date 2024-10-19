package com.groomiz.billage.classroom.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.groomiz.billage.classroom.dto.response.AdminClassroomDetailResponse;
import com.groomiz.billage.classroom.entity.Classroom;
import com.groomiz.billage.classroom.exception.ClassroomErrorCode;
import com.groomiz.billage.classroom.exception.ClassroomException;
import com.groomiz.billage.classroom.repository.ClassroomRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class AdminClassroomService {

	private final ClassroomRepository classroomRepository;

	@Transactional(readOnly = true)
	public AdminClassroomDetailResponse findClassroomByIdAndDate(Long classroomId, LocalDate date) {

		Classroom classroom = classroomRepository.findClassroomByIdAndDate(classroomId, date)
			.orElseThrow(() -> new ClassroomException(ClassroomErrorCode.CLASSROOM_NOT_FOUND));

		return AdminClassroomDetailResponse.from(classroom, date);
	}
}
