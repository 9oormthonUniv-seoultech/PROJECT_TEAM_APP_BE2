package com.groomiz.billage.classroom.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.groomiz.billage.classroom.entity.Classroom;

public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
}
