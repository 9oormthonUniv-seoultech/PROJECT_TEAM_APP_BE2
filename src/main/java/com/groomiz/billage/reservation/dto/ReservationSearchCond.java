package com.groomiz.billage.reservation.dto;

import com.groomiz.billage.global.dto.PageRequestDto;
import com.groomiz.billage.member.entity.Member;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ReservationSearchCond extends PageRequestDto {

	private Member requester;

	public ReservationSearchCond(Member requester) {
		this.requester = requester;
	}
}
