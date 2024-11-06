package com.groomiz.billage.classroom.repository;

import java.time.LocalDate;
import java.util.Optional;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.groomiz.billage.classroom.entity.Classroom;

public interface ClassroomRepository extends JpaRepository<Classroom, Long> {

	@Query("SELECT c FROM Classroom c "
		+ "JOIN FETCH c.reservations r "
		+ "JOIN FETCH r.reservationStatus rs "
		+ "WHERE c.id = :classroomId "
		+ "AND r.applyDate = :date "
		+ "AND rs.status in ('PENDING', 'APPROVED')")
	Optional<Classroom> findClassroomByIdAndDate(Long classroomId, LocalDate date);

	Optional<Classroom> findClassroomById(Long classroomId);

	List<Classroom> findClassroomByBuildingIdAndFloorAndCapacityGreaterThanEqual(Long buildingId, Long floor, Integer capacity);
}
