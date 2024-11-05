package com.groomiz.billage.reservation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.groomiz.billage.member.entity.Member;
import com.groomiz.billage.reservation.entity.ReservationGroup;
import com.groomiz.billage.reservation.entity.ReservationStatus;

public interface ReservationStatusRepository extends JpaRepository<ReservationStatus, Long> {
	List<ReservationStatus> findAllByRequester(Member requester);

	@Query("SELECT rs FROM ReservationStatus rs " +
		"JOIN FETCH rs.reservation r " +
		"JOIN FETCH r.classroom " +
		"WHERE rs.requester = :requester")
	List<ReservationStatus> findAllFetchJoinReservationByRequester(Member requester);

	@Query("SELECT rs "
		+ "FROM ReservationStatus rs "
		+ "JOIN FETCH rs.reservation r "
		+ "WHERE r.group = :group")
	List<ReservationStatus> findReservationStatusWithReservationByReservationGroup(ReservationGroup group);

	// 기간/반복 예약 일괄 관리자 취소로 변경하는 쿼리
	@Modifying
	@Query("UPDATE ReservationStatus rs " +
		"SET rs.status = 'ADMIN_CANCELED', rs.admin.id = :adminId " +
		"WHERE rs IN :statuses")
	void cancelAllByAdmin(List<ReservationStatus> statuses, Long adminId);
}
