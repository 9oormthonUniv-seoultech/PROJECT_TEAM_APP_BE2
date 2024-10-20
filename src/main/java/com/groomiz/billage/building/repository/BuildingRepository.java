package com.groomiz.billage.building.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.groomiz.billage.building.entity.Building;

public interface BuildingRepository extends JpaRepository<Building, Long> {

	@Query("SELECT DISTINCT b FROM Building b "
		+ "JOIN FETCH b.classrooms c "
		+ "WHERE (:buildingIds IS NULL OR b.id IN :buildingIds) "
		+ "AND (:floors IS NULL OR c.floor IN :floors)")
	List<Building> findBuildingsWithClassroomByIdsAndFloors(List<Long> buildingIds, List<Long> floors);
}
