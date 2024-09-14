package com.groomiz.billage.reservation.repository;

import com.groomiz.billage.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
