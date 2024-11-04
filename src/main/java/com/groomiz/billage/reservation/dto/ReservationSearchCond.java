package com.groomiz.billage.reservation.dto;

import java.util.ArrayList;
import java.util.List;

import com.groomiz.billage.global.dto.PageRequestDto;
import com.groomiz.billage.member.entity.Member;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ReservationSearchCond extends PageRequestDto {

	private Member admin;
	private List<Long> buildingIds = new ArrayList<>();

	@Builder
	public ReservationSearchCond(Member admin, List<Long> buildingIds) {
		this.admin = admin;
		this.buildingIds = buildingIds;
	}
}
