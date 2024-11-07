// package com.groomiz.billage.building;
//
// import static org.assertj.core.api.Assertions.*;
//
// import java.util.List;
//
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
//
// import com.groomiz.billage.building.entity.Building;
// import com.groomiz.billage.building.repository.BuildingRepository;
// import com.groomiz.billage.classroom.entity.Classroom;
// import com.groomiz.billage.classroom.repository.ClassroomRepository;
//
// @SpringBootTest
// class BuildingRepositoryTest {
//
// 	@Autowired
// 	private BuildingRepository buildingRepository;
//
// 	@Autowired
// 	private ClassroomRepository classroomRepository;
//
// 	private Building building1;
// 	private Building building2;
//
// 	@BeforeEach
// 	void setUp() {
// 		building1 = Building.builder()
// 			.name("창학관")
// 			.number("3")
// 			.startFloor(1L)
// 			.endFloor(5L)
// 			.imageUrl("image_url_1")
// 			.build();
//
// 		building2 = Building.builder()
// 			.name("미래관")
// 			.number("60")
// 			.startFloor(1L)
// 			.endFloor(7L)
// 			.imageUrl("image_url_2")
// 			.build();
//
// 		buildingRepository.save(building1);
// 		buildingRepository.save(building2);
//
// 		Classroom classroom1 = Classroom.builder()
// 			.name("전자공학과 강의실")
// 			.number("201")
// 			.floor(2L)
// 			.description("전자공학과 강의실")
// 			.capacity(40)
// 			.building(building1)
// 			.build();
//
// 		Classroom classroom2 = Classroom.builder()
// 			.name("컴퓨터공학과 실습실")
// 			.number("301")
// 			.floor(3L)
// 			.description("컴퓨터공학과 실습실")
// 			.capacity(60)
// 			.building(building2)
// 			.build();
//
// 		classroomRepository.save(classroom1);
// 		classroomRepository.save(classroom2);
// 	}
//
// 	@Test
// 	@DisplayName("BuildingRepository: 수용 가능한 건물 조회")
// 	void findBuildingsByCapacity() {
// 		// 인원 50명 기준으로 조회 (창학관은 제외되고 미래관만 조회되어야 함)
// 		List<Building> buildings = buildingRepository.findBuildingsByCapacity(50);
//
// 		assertThat(buildings).hasSize(1);
// 		assertThat(buildings.get(0).getName()).isEqualTo("미래관");
// 	}
// }