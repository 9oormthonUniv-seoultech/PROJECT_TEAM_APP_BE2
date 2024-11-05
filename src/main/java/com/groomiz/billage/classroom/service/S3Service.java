package com.groomiz.billage.classroom.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket-name}")
    private String bucketName;

    public S3Service(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    // 이미지 이름 중복 방지를 위한 이름 변경 메서드
    private String generateUniqueFileName(String originalName) {
        String uuid = UUID.randomUUID().toString();
        return uuid + "_" + originalName;
    }

    // 이미지 파일을 S3에 업로드하고 URL 반환
    public String uploadFile(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        String fileName = generateUniqueFileName(originalFileName);

        // 파일 확장자 추출 및 메타데이터 설정
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image/" + fileExtension);

        try {
            // S3에 파일 업로드, 공개 접근 설정
            amazonS3.putObject(new PutObjectRequest(bucketName, fileName, file.getInputStream(), metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));

            // 업로드된 파일의 URL 반환
            return amazonS3.getUrl(bucketName, fileName).toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
