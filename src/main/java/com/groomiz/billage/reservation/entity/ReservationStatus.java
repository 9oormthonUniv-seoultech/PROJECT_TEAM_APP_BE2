package com.groomiz.billage.reservation.entity;

import org.hibernate.annotations.ColumnDefault;

import com.groomiz.billage.common.entity.BaseEntity;
import com.groomiz.billage.member.entity.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationStatus extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ReservationStatusType status = ReservationStatusType.PENDING;

	@Column(name = "rejection_reason")
	private String rejectionReason;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "admin_id")
	private Member admin;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "requester_id", nullable = false)
	private Member requester;

	@OneToOne(mappedBy = "reservationStatus")
	private Reservation reservation;

	public ReservationStatus (Member requester) {
		this.requester = requester;
	}

	public void updateStatus(ReservationStatusType status) {
		this.status = status;
	}

	public boolean isApproved() {
		return this.status == ReservationStatusType.APPROVED;
	}

	public boolean isPending() {
		return this.status == ReservationStatusType.PENDING;
	}

	public boolean isRejected() {
		return this.status == ReservationStatusType.REJECTED;
	}

	public boolean isStudentCanceled() {
		return this.status == ReservationStatusType.STUDENT_CANCELED;
	}

	public boolean isAdminCanceled() {
		return this.status == ReservationStatusType.ADMIN_CANCELED;
	}
}