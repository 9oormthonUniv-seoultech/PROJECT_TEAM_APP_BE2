package com.groomiz.billage.classroom.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.groomiz.billage.classroom.entity.Classroom;

public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
	Optional<List<Classroom>> findByBuildingIdAndFloorAndCapacityGreaterThanEqual(Long buildingId, Long floor, Integer capacity);
}