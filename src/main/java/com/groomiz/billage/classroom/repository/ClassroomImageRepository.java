package com.groomiz.billage.classroom.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.groomiz.billage.classroom.entity.ClassroomImage;

public interface ClassroomImageRepository extends JpaRepository<ClassroomImage, Long> {
}
