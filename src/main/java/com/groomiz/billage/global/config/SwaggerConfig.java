package com.groomiz.billage.global.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {
	@Bean
	public OpenAPI openAPI() {
		Info info = new Info()
			.title("빌리지 API Document")
			.version("1.0")
			.description(
				"환영합니다! [발리지](https://example.com)는 서울과학기술대학교 강의실을 빌리기 위해서 위해 만들어진 플랫폼입니다. 이 API 문서는 빌리지의 API를 사용하는 방법을 설명합니다.\n")
			.contact(new io.swagger.v3.oas.models.info.Contact().email("billage.official@gmail.com"));

		String jwtScheme = "jwtAuth";
		SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtScheme);
		Components components = new Components()
			.addSecuritySchemes(jwtScheme, new SecurityScheme()
				.name("Authorization")
				.type(SecurityScheme.Type.HTTP)
				.in(SecurityScheme.In.HEADER)
				.scheme("Bearer")
				.bearerFormat("JWT"));

		return new OpenAPI()
			.addServersItem(new Server().url("http://localhost:8080"))
			.components(new Components())
			.info(info)
			.addSecurityItem(securityRequirement)
			.components(components);
	}

	@Bean
	public GroupedOpenApi studentGroup() {
		return GroupedOpenApi.builder()
			.group("학생")
			.pathsToMatch("/api/v1/users/**", "/api/v1/univ/building/**", "/api/v1/univ/classroom/**",
				"/api/v1/univ/**",
				"/api/v1/reservations/**")
			.build();
	}

	@Bean
	public GroupedOpenApi adminGroup() {
		return GroupedOpenApi.builder()
			.group("관리자")
			.pathsToMatch("/api/v1/admin/classrooms/**", "/api/v1/admin/reservations/**")
			.build();
	}
}