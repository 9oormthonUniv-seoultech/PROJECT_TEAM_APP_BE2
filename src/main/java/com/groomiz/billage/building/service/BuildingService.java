
package com.groomiz.billage.building.service;

import java.time.LocalDate;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

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

	@Transactional(readOnly = true)
	public List<BuildingListResponse> findAllBuildings(Integer count) {
		// 예시: 건물 목록을 Building 엔티티에서 조회
		List<Building> buildings = buildingRepository.findBuildingsByCapacity(count)
			.orElseThrow(() -> new BuildingException(BuildingErrorCode.BUILDING_NOT_FOUND));


		// 조회된 Building 리스트를 BuildingListResponse로 변환
		return buildings.stream()
			.map(building -> new BuildingListResponse(
				building.getId(),
				building.getName(),
				building.getNumber(),
				generateFloors(building.getStartFloor(), building.getEndFloor())
			))
			.collect(Collectors.toList());
	}

	private List<Long> generateFloors(Long startFloor, Long endFloor) {
		return LongStream.rangeClosed(startFloor, endFloor)
			.boxed()
			.collect(Collectors.toList());
	}
}
