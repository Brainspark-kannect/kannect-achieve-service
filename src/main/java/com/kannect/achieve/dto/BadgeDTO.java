package com.kannect.achieve.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BadgeDTO {
	private Long id;
	private String badgeType;
	private Integer recognitionPoints;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}