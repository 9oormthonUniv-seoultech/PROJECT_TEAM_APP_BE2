package com.groomiz.billage.building.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.groomiz.billage.building.entity.Building;

import io.lettuce.core.dynamic.annotation.Param;

public interface BuildingRepository extends JpaRepository<Building, Long> {

	// 특정 날짜와 인원에 맞는 수용 가능한 건물 조회 (예약 여부는 무시)
	@Query("SELECT DISTINCT b FROM Building b " +
		"JOIN Classroom c ON c.building.id = b.id " +
		"WHERE c.capacity >= :headcount")
	Optional<List<Building>> findBuildingsByCapacity(@Param("headcount") Integer headcount);
}
