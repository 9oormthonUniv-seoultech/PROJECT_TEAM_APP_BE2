package com.groomiz.billage.global.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "예제 요청 DTO")
public class ExampleReqDto {
	@Schema(description = "이름", example = "홍길동")
	private String name;
	@Schema(description = "나이", example = "20")
	private int age;
}
