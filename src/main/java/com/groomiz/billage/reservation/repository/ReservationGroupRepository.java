package com.groomiz.billage.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.groomiz.billage.reservation.entity.ReservationGroup;

public interface ReservationGroupRepository extends JpaRepository<ReservationGroup, Long> {
}
