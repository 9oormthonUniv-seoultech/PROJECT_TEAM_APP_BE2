package com.groomiz.billage.reservation.repository;

import org.springframework.data.domain.Page;

import com.groomiz.billage.reservation.dto.ReservationSearchCond;
import com.groomiz.billage.reservation.dto.response.AdminReservationStatusListResponse.ReservationInfo;

public interface ReservationRepositoryCustom {

	Page<ReservationInfo> searchPendingReservationPageByBuilding(ReservationSearchCond reservationSearchCond);
	Page<ReservationInfo> searchApprovedReservationPageByAdmin(ReservationSearchCond reservationSearchCond);
	Page<ReservationInfo> searchRejectedAndCanceledReservationPageByAdmin(ReservationSearchCond reservationSearchCond);
}
