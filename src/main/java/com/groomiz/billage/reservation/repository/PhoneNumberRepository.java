package com.groomiz.billage.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.groomiz.billage.reservation.entity.PhoneNumber;

public interface PhoneNumberRepository extends JpaRepository<PhoneNumber, Long> {
}
