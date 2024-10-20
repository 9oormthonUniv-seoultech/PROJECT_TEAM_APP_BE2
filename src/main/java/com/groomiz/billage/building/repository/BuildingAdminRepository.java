package com.groomiz.billage.building.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.groomiz.billage.building.entity.BuildingAdmin;
import com.groomiz.billage.member.entity.Member;

public interface BuildingAdminRepository extends JpaRepository<BuildingAdmin, Long> {

	@Query("SELECT ba.building.id FROM BuildingAdmin ba WHERE ba.admin = :admin")
	List<Long> findAllBuildingIdByAdmin(Member admin);
}
