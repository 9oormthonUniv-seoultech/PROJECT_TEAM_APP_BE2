package com.groomiz.billage.classroom;

import com.groomiz.billage.building.entity.Building;
import com.groomiz.billage.building.repository.BuildingRepository;
import com.groomiz.billage.classroom.entity.Classroom;
import com.groomiz.billage.classroom.repository.ClassroomRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@SpringBootTest
class ClassroomRepositoryTest {

	@Autowired
	private BuildingRepository buildingRepository;

	@Autowired
	private ClassroomRepository classroomRepository;

	private Building building1;
	private Building building2;

	@BeforeEach
	void setUp() {
		building1 = Building.builder()
			.name("창학관")
			.number("3")
			.startFloor(1L)
			.endFloor(5L)
			.imageUrl("image_url_1")
			.build();

		building2 = Building.builder()
			.name("미래관")
			.number("60")
			.startFloor(1L)
			.endFloor(7L)
			.imageUrl("image_url_2")
			.build();

		buildingRepository.save(building1);
		buildingRepository.save(building2);

		Classroom classroom1 = Classroom.builder()
			.name("전자공학과 강의실")
			.number("101")
			.floor(2L)
			.description("전자공학과 강의실")
			.capacity(40)
			.building(building1)
			.build();

		Classroom classroom2 = Classroom.builder()
			.name("컴퓨터공학과 실습실")
			.number("301")
			.floor(3L)
			.description("컴퓨터공학과 실습실")
			.capacity(60)
			.building(building2)
			.build();

		Classroom classroom3 = Classroom.builder()
			.name("컴퓨터공학과 강의실")
			.number("302")
			.floor(3L)
			.description("컴퓨터공학과 강의실")
			.capacity(31)
			.building(building2)
			.build();

		classroomRepository.save(classroom1);
		classroomRepository.save(classroom2);
		classroomRepository.save(classroom3);
	}

	@Test
	@Rollback(value = false)
	@DisplayName("ClassroomRepository: 강의실 수용인원 이상 강의실 조회")
	void findClassroomByCapacity() {
		List<Classroom> classrooms = classroomRepository.findByBuildingIdAndFloorAndCapacityGreaterThanEqual(building2.getId(), 3L, 30);
		assertThat(classrooms).hasSize(2);
		assertThat(classrooms.get(0).getName()).isEqualTo("컴퓨터공학과 실습실");
		assertThat(classrooms.get(1).getName()).isEqualTo("컴퓨터공학과 강의실");
	}
}
