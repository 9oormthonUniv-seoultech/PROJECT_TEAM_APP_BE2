package com.groomiz.billage.global.dto;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PageRequestDto {
	private static final int DEFAULT_PAGE = 1;
	private static final int DEFAULT_SIZE = 20;

	private int page = DEFAULT_PAGE;
	private int size = DEFAULT_SIZE;

	public void setPage(int page) {
		this.page = Math.max(page, 1);
	}

	public void setSize(int size) {
		this.size = Math.max(size, 1);
	}

	public Pageable toPageable() {
		return PageRequest.of(page - 1, size);
	}
}
