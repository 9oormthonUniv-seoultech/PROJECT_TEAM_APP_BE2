package com.groomiz.billage.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.groomiz.billage.reservation.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
