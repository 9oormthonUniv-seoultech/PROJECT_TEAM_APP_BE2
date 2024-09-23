package com.groomiz.billage.global.document;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.groomiz.billage.global.dto.ExampleReqDto;
import com.groomiz.billage.global.dto.ExampleResDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/example")
@RequiredArgsConstructor
@Tag(name = "Exception Document", description = "예제 에러코드 문서화")
public class ExampleController {
	@GetMapping("/global")
	@Operation(summary = "글로벌 (aop, 서버 내부 오류등)  관련 에러 코드 나열")
	public ExampleResDto example(ExampleReqDto exampleReqDto) {
		ExampleResDto exampleResDto = new ExampleResDto();
		exampleResDto.setName(exampleReqDto.getName());
		exampleResDto.setAge(exampleReqDto.getAge());
		return exampleResDto;
	}
}
