package com.kannect.achieve.dto.mapper;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.kannect.achieve.dto.TaskDTO;
import com.kannect.achieve.entity.Task;

@Component
public class TaskMapper {

	private final ModelMapper modelMapper = new ModelMapper();

	public Task mapToTask(TaskDTO taskDTO) {
		return modelMapper.map(taskDTO, Task.class);

	}

	public TaskDTO mapToTaskDTO(Task task) {
		return modelMapper.map(task, TaskDTO.class);
	}

	public List<TaskDTO> mapToTaskDTOs(List<Task> tasks) {
		List<TaskDTO> dtos = new ArrayList<>();
		for (Task task : tasks) {
			dtos.add(mapToTaskDTO(task));
		}
		return dtos;
	}

	public Task mapToTask(Task task, TaskDTO taskDTO) {
		modelMapper.map(taskDTO, task);
		return task;
	}

}
