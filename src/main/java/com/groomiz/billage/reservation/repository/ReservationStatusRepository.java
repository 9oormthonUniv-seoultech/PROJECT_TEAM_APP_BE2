package com.groomiz.billage.reservation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.groomiz.billage.member.entity.Member;
import com.groomiz.billage.reservation.dto.response.ReservationStatusListResponse;
import com.groomiz.billage.reservation.entity.ReservationStatus;

public interface ReservationStatusRepository extends JpaRepository<ReservationStatus, Long> {
	List<ReservationStatus> findAllByRequester(Member requester);

	@Query("SELECT rs FROM ReservationStatus rs " +
		"JOIN FETCH rs.reservation r " +
		"JOIN FETCH r.classroom " +
		"WHERE rs.requester = :requester")
	List<ReservationStatus> findAllFetchJoinReservationByRequester(Member requester);

}
