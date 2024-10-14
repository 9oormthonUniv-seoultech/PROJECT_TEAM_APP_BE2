package com.groomiz.billage.reservation.repository;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.annotations.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.groomiz.billage.classroom.dto.ReservationTime;
import com.groomiz.billage.reservation.entity.Reservation;

import io.lettuce.core.dynamic.annotation.Param;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

	@Query("SELECT new com.groomiz.billage.classroom.dto.ReservationTime(r.startTime, r.endTime) "
		+ "FROM Reservation r "
		+ "WHERE r.reservationStatus.status IN ('PENDING', 'APPROVED') "
		+ "AND r.applyDate = :applyDate "
		+ "AND r.classroom.id = :classroomId")
	List<ReservationTime> findPendingOrApprovedReservationsByDateAndClassroom(@Param("applyDate") LocalDate applyDate, @Param("classroomId") Long classroomId);

}
