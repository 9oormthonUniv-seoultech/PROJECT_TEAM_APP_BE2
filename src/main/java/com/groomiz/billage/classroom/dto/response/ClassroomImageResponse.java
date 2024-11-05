package com.groomiz.billage.classroom.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Schema(description = "강의실 이미지 업로드 DTO")
@AllArgsConstructor
public class ClassroomImageResponse {

    @Schema(description = "이미지 ID", example = "1")
    private Long imageId;
    @Schema(description = "강의실 ID", example = "101")
    private Long classroomId;
    @Schema(description = "이미지 URL", example = "https://groomiz.com/classroom/1.jpg")
    private String imageUrl;
}
