package com.groomiz.billage.classroom.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.groomiz.billage.classroom.dto.response.ClassroomImageResponse;
import com.groomiz.billage.classroom.entity.Classroom;
import com.groomiz.billage.classroom.entity.ClassroomImage;
import com.groomiz.billage.classroom.exception.ClassroomErrorCode;
import com.groomiz.billage.classroom.exception.ClassroomException;
import com.groomiz.billage.classroom.repository.ClassroomImageRepository;
import com.groomiz.billage.classroom.repository.ClassroomRepository;
import com.groomiz.billage.global.config.S3Config;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class ClassroomImageService {

	private final S3Service s3Service;
	private final ClassroomRepository classroomRepository;
	private final ClassroomImageRepository classroomImageRepository;
	private final S3Config s3Config;

	public ClassroomImageResponse uploadClassroomImage(Long classroomId, MultipartFile imageFile) throws Exception {
		//Classroom 엔티티 찾기
		Classroom classroom = classroomRepository.findById(classroomId)
			.orElseThrow(() -> new ClassroomException(ClassroomErrorCode.CLASSROOM_NOT_FOUND));

		String fileName = UUID.randomUUID() + imageFile.getOriginalFilename();
		// 파일데이터와 파일명 넘겨서 S3에 저장

		String imageUrl = s3Service.uploadFile(imageFile, fileName);

		ClassroomImage classroomImage = ClassroomImage.builder()
			.classroom(classroom)
			.imageUrl(imageUrl)
			.build();

		classroomImageRepository.save(classroomImage);

		return new ClassroomImageResponse(classroomImage.getId(), classroomId, classroomImage.getImageUrl());

	}
}

