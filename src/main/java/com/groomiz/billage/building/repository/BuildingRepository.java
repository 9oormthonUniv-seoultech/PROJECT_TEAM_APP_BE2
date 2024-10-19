package com.groomiz.billage.building.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.groomiz.billage.building.entity.Building;

import io.lettuce.core.dynamic.annotation.Param;

public interface BuildingRepository extends JpaRepository<Building, Long> {

	// 날짜와 인원을 기준으로 예약 가능한 건물 목록을 조회하는 JPQL 쿼리
	@Query("SELECT DISTINCT b FROM Building b " +
		"JOIN Classroom c ON c.building.id = b.id " +
		"LEFT JOIN Reservation r ON r.classroom.id = c.id AND r.applyDate = :date " +
		"WHERE r.id IS NULL AND c.capacity >= :headcount")
	Optional<List<Building>> findAvailableBuildings(@Param("date") LocalDate date, @Param("headcount") Integer headcount);

}

