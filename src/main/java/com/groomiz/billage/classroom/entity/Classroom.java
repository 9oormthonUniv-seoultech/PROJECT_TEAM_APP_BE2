package com.groomiz.billage.classroom.entity;

import com.groomiz.billage.building.entity.Building;
import com.groomiz.billage.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Classroom extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String number;

	@Column(nullable = false)
	private Long floor;

	private String description;

	private Integer capacity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "building_id", nullable = false)
	private Building building;

	@Builder
	public Classroom(String name, String number, Long floor, String description, Integer capacity, Building building) {
		this.name = name;
		this.number = number;
		this.floor = floor;
		this.description = description;
		this.capacity = capacity;
		this.building = building;
	}
}