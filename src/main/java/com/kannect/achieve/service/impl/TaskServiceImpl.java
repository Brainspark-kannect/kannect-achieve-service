package com.kannect.achieve.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.kannect.achieve.dto.TaskDTO;
import com.kannect.achieve.dto.mapper.TaskMapper;
import com.kannect.achieve.entity.Task;
import com.kannect.achieve.exception.ResourceNotFoundException;
import com.kannect.achieve.repository.TaskRepository;
import com.kannect.achieve.service.EmailService;
import com.kannect.achieve.service.TaskService;
import com.kannect.user.auth.entity.User;
import com.kannect.user.auth.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

	private final TaskRepository taskRepository;
	private final TaskMapper taskMapper;
	public static final Logger LOGGER = LoggerFactory.getLogger(TaskServiceImpl.class);
	private final UserRepository userRepository;
	private final EmailService emailService;

	@Override
	public TaskDTO assignTask(TaskDTO taskDTO) {
		Task task = taskMapper.mapToTask(taskDTO);
		task.setStatus("PENDING");
		task.setCreatedAt(LocalDateTime.now());
		task.setUpdatedAt(LocalDateTime.now());
		Task savedTask = taskRepository.save(task);

		// Fetch email info
		User assignedUser = userRepository.findById(task.getAssignedTo())
				.orElseThrow(() -> new RuntimeException("Assigned user not found"));
		User hrUser = userRepository.findById(task.getAssignedBy())
				.orElseThrow(() -> new RuntimeException("HR user not found"));

		String subject = "New Task Assigned: " + task.getTitle();
		String body = "Hello " + assignedUser.getFirstName() + ",\n\n" + "You have been assigned a new task titled \""
				+ task.getTitle() + "\" with deadline " + task.getDeadline() + ".\n\n" + "Description: "
				+ task.getDescription();

		emailService.sendEmail(List.of(assignedUser.getEmail()), List.of(hrUser.getEmail()), subject, body);

		return taskMapper.mapToTaskDTO(savedTask);
	}

	@Override
	public List<TaskDTO> getTasksForUser(Long userId) {
		return taskMapper.mapToTaskDTOs(taskRepository.findByUserId(userId));
	}

	@Override
	public List<TaskDTO> getAllTasks() {
		return taskMapper.mapToTaskDTOs(taskRepository.findAll());
	}

	@Override
	public TaskDTO getTaskById(Long id) {
		Optional<Task> taskOptional = taskRepository.findById(id);
		if (taskOptional.isEmpty()) {
			LOGGER.error("Task not found for id: " + id);
			throw new ResourceNotFoundException("Task not found for id: " + id);
		}

		return taskMapper.mapToTaskDTO(taskOptional.get());
	}

	@Override
	public TaskDTO updateTaskStatus(Long taskId, String status) {
		Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));

		task.setUpdatedAt(LocalDateTime.now());
		LocalDateTime now = LocalDateTime.now();

		User assignedUser = userRepository.findById(task.getAssignedTo())
				.orElseThrow(() -> new RuntimeException("Assigned user not found"));
		User hrUser = userRepository.findById(task.getAssignedBy())
				.orElseThrow(() -> new RuntimeException("HR user not found"));

		if ("COMPLETED".equalsIgnoreCase(status)) {
			if (now.isAfter(task.getDeadline())) {
				task.setStatus("OVERDUE");
			} else {
				task.setStatus("COMPLETED");
			}
			task.setCompletedAt(now);

			emailService.sendEmail(List.of(hrUser.getEmail()), List.of(assignedUser.getEmail()),
					"Task Completed: " + task.getTitle(), "The task titled \"" + task.getTitle()
							+ "\" has been completed by " + assignedUser.getFirstName() + ".");

		} else if ("APPROVED".equalsIgnoreCase(status)) {
			task.setStatus("APPROVED");

			emailService.sendEmail(List.of(assignedUser.getEmail()), List.of(hrUser.getEmail()),
					"Task Approved: " + task.getTitle(),
					"Your task \"" + task.getTitle() + "\" has been approved by HR.");
		} else {
			task.setStatus(status);
		}

		return taskMapper.mapToTaskDTO(taskRepository.save(task));
	}

	@Override
	public TaskDTO updateTask(Long taskId, TaskDTO taskDTO) {
	    Task task = taskRepository.findById(taskId)
	        .orElseThrow(() -> new RuntimeException("Task not found"));

	    task = taskMapper.mapToTask(task, taskDTO);
	    task.setUpdatedAt(LocalDateTime.now());

	    Task updatedTask = taskRepository.save(task);

	    User assignedUser = userRepository.findById(task.getAssignedTo())
	        .orElseThrow(() -> new RuntimeException("Assigned user not found"));
	    User hrUser = userRepository.findById(task.getAssignedBy())
	        .orElseThrow(() -> new RuntimeException("HR user not found"));

	    emailService.sendEmail(List.of(assignedUser.getEmail()), List.of(hrUser.getEmail()),
	        "Task Updated: " + task.getTitle(),
	        "Your task \"" + task.getTitle() + "\" has been updated by HR. Please check the details.");

	    return taskMapper.mapToTaskDTO(updatedTask);
	}


	@Override
	public List<TaskDTO> getTaskByStatus(List<String> status) {
		return taskMapper.mapToTaskDTOs(taskRepository.findByStatusIn(status));
	}

}
