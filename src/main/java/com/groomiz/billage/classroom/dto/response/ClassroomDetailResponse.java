package com.groomiz.billage.classroom.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import com.groomiz.billage.classroom.dto.ReservationTime;
import com.groomiz.billage.classroom.entity.Classroom;
import com.groomiz.billage.classroom.entity.ClassroomImage;
import com.groomiz.billage.reservation.entity.Reservation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Schema(description = "강의실 상세 조회 응답 DTO")
public class ClassroomDetailResponse {

	@Schema(description = "강의실 ID", example = "1")
	private Long classroomId;
	@Schema(description = "강의실 이름", example = "실험실")
	private String classroomName;
	@Schema(description = "강의실 번호", example = "101")
	private String classroomNumber;
	@Schema(description = "수용 인원", example = "30")
	private Integer capacity;
	@Schema(description = "강의실 설명", example = "빔 프로젝터, 컴퓨터, 사물함")
	private String description;
	@Schema(description = "강의실 이미지", example = "https://groomiz.com/classroom/1.jpg")
	private List<String> classroomImages;
	@Schema(description = "예약된 시간", example = "[{\"startTime\":\"09:00\",\"endTime\":\"10:00\"}]")
	private List<ReservationTime> reservationTimes;

	@Builder
	public ClassroomDetailResponse(Long classroomId, String classroomName, String classroomNumber, Integer capacity,
		String description, List<String> classroomImages, List<ReservationTime> reservationTimes) {
		this.classroomId = classroomId;
		this.classroomName = classroomName;
		this.classroomNumber = classroomNumber;
		this.capacity = capacity;
		this.description = description;
		this.classroomImages = classroomImages;
		this.reservationTimes = reservationTimes;
	}

	// 강의실 정보와 예약 정보를 외부에서 주입받아 ClassroomDetailResponse 생성
	public static ClassroomDetailResponse from(Classroom classroom, List<Reservation> reservations) {

		List<ClassroomImage> images = classroom.getImages();

		return ClassroomDetailResponse.builder()
			.classroomId(classroom.getId())
			.classroomName(classroom.getName())
			.classroomNumber(classroom.getNumber())
			.capacity(classroom.getCapacity())
			.description(classroom.getDescription())
			.classroomImages(images.stream()
				.map(ClassroomImage::getImageUrl)
				.collect(Collectors.toList()))
			.reservationTimes(
				reservations.stream()
					.map(reservation -> new ReservationTime(reservation.getStartTime(), reservation.getEndTime()))
					.collect(Collectors.toList())
			)
			.build();
	}
}
