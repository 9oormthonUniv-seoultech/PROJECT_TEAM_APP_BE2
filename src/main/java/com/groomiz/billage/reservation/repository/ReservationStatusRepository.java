package com.groomiz.billage.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.groomiz.billage.reservation.entity.ReservationStatus;

public interface ReservationStatusRepository extends JpaRepository<ReservationStatus, Long> {
}
