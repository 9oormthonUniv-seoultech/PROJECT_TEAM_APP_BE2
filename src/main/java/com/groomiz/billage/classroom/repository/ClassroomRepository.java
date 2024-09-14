package com.groomiz.billage.classroom.repository;

import com.groomiz.billage.classroom.entity.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
}
