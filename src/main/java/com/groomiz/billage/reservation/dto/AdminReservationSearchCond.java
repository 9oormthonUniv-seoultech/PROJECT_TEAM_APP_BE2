package com.groomiz.billage.reservation.dto;

import java.util.ArrayList;
import java.util.List;

import com.groomiz.billage.building.entity.Building;
import com.groomiz.billage.global.dto.PageRequestDto;
import com.groomiz.billage.member.entity.Member;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AdminReservationSearchCond extends PageRequestDto {

	private Member admin;
	private List<Building> buildings = new ArrayList<>();

	@Builder
	public AdminReservationSearchCond(Member admin, List<Building> buildings) {
		this.admin = admin;
		this.buildings = buildings;
	}
}
