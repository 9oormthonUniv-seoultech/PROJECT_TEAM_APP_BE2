
package com.groomiz.billage.building.service;

import java.time.LocalDate;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.groomiz.billage.building.dto.response.BuildingListResponse;
import com.groomiz.billage.building.entity.Building;
import com.groomiz.billage.building.exception.BuildingErrorCode;
import com.groomiz.billage.building.exception.BuildingException;

import com.groomiz.billage.building.repository.BuildingRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class BuildingService {
	private final BuildingRepository buildingRepository;

	public List<BuildingListResponse> findAllBuildings(LocalDate date, Integer count) {
		// 예시: 건물 목록을 Building 엔티티에서 조회
		List<Building> buildings = buildingRepository.findAvailableBuildings(date, count)
			.orElseThrow(() -> new BuildingException(BuildingErrorCode.BUILDING_NOT_FOUND));


		// 조회된 Building 리스트를 BuildingListResponse로 변환
		return buildings.stream()
			.map(building -> new BuildingListResponse(
				building.getId(),
				building.getName(),
				building.getNumber()
			))
			.collect(Collectors.toList());
	}
}
