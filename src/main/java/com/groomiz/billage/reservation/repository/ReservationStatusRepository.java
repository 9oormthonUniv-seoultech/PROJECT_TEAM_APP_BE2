package com.groomiz.billage.reservation.repository;


import com.groomiz.billage.reservation.entity.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationStatusRepository extends JpaRepository<ReservationStatus, Long> {
}
