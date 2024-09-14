package com.groomiz.billage.reservation.repository;


import com.groomiz.billage.reservation.entity.PhoneNumber;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhoneNumberRepository extends JpaRepository<PhoneNumber, Long> {
}
