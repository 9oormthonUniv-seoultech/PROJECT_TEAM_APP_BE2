package com.groomiz.billage.classroom.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.net.URLDecoder;


@RequiredArgsConstructor

@Service
public class S3Service {
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket-name}")
    private String bucketName;

    public String uploadFile(MultipartFile imageFile, String s3FileName) throws IOException {
        // 메타데이터 생성
        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentLength(imageFile.getInputStream().available());
        // S3에 객체 등록
        amazonS3.putObject(bucketName, s3FileName, imageFile.getInputStream(), objMeta);
        // 등록된 객체의 url 반환
        return URLDecoder.decode(amazonS3.getUrl(bucketName, s3FileName).toString(), "utf-8");
    }
}
