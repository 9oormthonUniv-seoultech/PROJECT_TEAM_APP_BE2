package com.groomiz.billage.reservation.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.groomiz.billage.classroom.dto.ReservationTime;
import com.groomiz.billage.member.entity.Member;
import com.groomiz.billage.reservation.dto.response.AdminReservationResponse;
import com.groomiz.billage.reservation.entity.Reservation;

import io.lettuce.core.dynamic.annotation.Param;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

	@Query("SELECT new com.groomiz.billage.classroom.dto.ReservationTime(r.startTime, r.endTime) "
		+ "FROM Reservation r "
		+ "WHERE r.reservationStatus.status IN ('PENDING', 'APPROVED') "
		+ "AND r.applyDate = :applyDate "
		+ "AND r.classroom.id = :classroomId")
	List<ReservationTime> findPendingOrApprovedReservationsByDateAndClassroom(@Param("applyDate") LocalDate applyDate, @Param("classroomId") Long classroomId);

	@Query("SELECT new com.groomiz.billage.reservation.dto.response.AdminReservationResponse(r.applyDate, r.headcount, b.name, c.name, rs.requester.phoneNumber ,r.phoneNumber, r.purpose, r.startTime, r.endTime,r.contents) "
		+ "FROM Reservation r "
		+ "JOIN r.classroom c "
		+ "JOIN c.building b "
		+ "JOIN r.reservationStatus rs "
		+ "WHERE r.id = :reservationId")
	Optional<AdminReservationResponse> findAdminReservationResponseById(@Param("reservationId") Long reservationId);

	@Query("SELECT r "
	+ "FROM Reservation r "
	+ "JOIN FETCH r.reservationStatus rs "
	+ "WHERE rs.status = 'PENDING'"
	+ "AND r.classroom.building.id in :buildingIds")
	List<Reservation> findPendingReservationsWithReservationStatusByBuildingIds(List<Long> buildingIds);

	@Query("SELECT r "
		+ "FROM Reservation r "
		+ "JOIN FETCH r.reservationStatus rs "
		+ "WHERE rs.status = 'APPROVED'"
		+ "AND rs.admin = :admin")
	List<Reservation> findApprovedReservationsWithReservationStatusByAdmin(Member admin);

	@Query("SELECT r "
		+ "FROM Reservation r "
		+ "JOIN FETCH r.reservationStatus rs "
		+ "WHERE rs.status IN ('REJECTED', 'ADMIN_CANCELED', 'STUDENT_CANCELED') "
		+ "AND rs.admin = :admin")
	List<Reservation> findRejectedCanceledReservationsWithReservationStatusByAdmin(@Param("admin") Member admin);
}
