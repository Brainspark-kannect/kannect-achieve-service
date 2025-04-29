package com.kannect.achieve.service;

import java.util.List;

import com.kannect.achieve.dto.TaskDTO;

public interface TaskService {

	TaskDTO assignTask(TaskDTO taskDTO);

	List<TaskDTO> getTasksForUser(Long userId);

	TaskDTO getTaskById(Long id);

	TaskDTO updateTaskStatus(Long taskId, String status);

	TaskDTO updateTask(Long taskId, TaskDTO taskDTO);

	List<TaskDTO> getTaskByStatus(List<String> status);

	List<TaskDTO> getAllTasks();

}
