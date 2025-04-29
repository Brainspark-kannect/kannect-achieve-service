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
public class TaskDTO {
	private Long id;
	private String title;
	private String description;
	private Long assignedTo;
	private Long assignedBy;
	private LocalDateTime deadline;
	private LocalDateTime completedAt;
	private String status;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
