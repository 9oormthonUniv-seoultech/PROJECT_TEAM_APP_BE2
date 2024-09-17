package com.groomiz.billage.building.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.groomiz.billage.building.entity.Building;

public interface BuildingRepository extends JpaRepository<Building, Long> {

}
