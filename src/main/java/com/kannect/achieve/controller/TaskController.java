package com.kannect.achieve.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kannect.achieve.dto.TaskDTO;
import com.kannect.achieve.dto.response.SuccessResponse;
import com.kannect.achieve.interfaces.ITaskController;
import com.kannect.achieve.service.TaskService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tasks")
@CrossOrigin(origins = "*")
@Validated
@RequiredArgsConstructor
public class TaskController implements ITaskController {

    private final TaskService taskService;

    @Override
    @PostMapping("/assign")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<SuccessResponse> assignTask(@RequestBody TaskDTO taskDTO) {
        TaskDTO created = taskService.assignTask(taskDTO);
        return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(), HttpStatus.OK, "Task assigned successfully", created));
    }

    @Override
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'EMPLOYEE')")
    public ResponseEntity<SuccessResponse> getTasksByUserId(@PathVariable Long userId) {
        List<TaskDTO> tasks = taskService.getTasksForUser(userId);
        return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(), HttpStatus.OK, "Tasks fetched successfully", tasks));
    }

    @Override
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<SuccessResponse> getAllTasks() {
        return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(), HttpStatus.OK, "All tasks fetched successfully", taskService.getAllTasks()));
    }

    @Override
    @GetMapping("/{taskId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'EMPLOYEE')")
    public ResponseEntity<SuccessResponse> getTaskById(@PathVariable Long taskId) {
        TaskDTO task = taskService.getTaskById(taskId);
        return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(), HttpStatus.OK, "Task fetched successfully", task));
    }

    @Override
    @PutMapping("/{taskId}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'EMPLOYEE')")
    public ResponseEntity<SuccessResponse> updateTaskStatus(@PathVariable Long taskId, @RequestParam String status) {
        TaskDTO updated = taskService.updateTaskStatus(taskId, status);
        return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(), HttpStatus.OK, "Task status updated", updated));
    }

    @Override
    @PutMapping("/{taskId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<SuccessResponse> updateTask(@PathVariable Long taskId, @RequestBody TaskDTO taskDTO) {
        TaskDTO updated = taskService.updateTask(taskId, taskDTO);
        return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(), HttpStatus.OK, "Task updated successfully", updated));
    }

    @Override
    @GetMapping("/by-status")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'EMPLOYEE')")
    public ResponseEntity<SuccessResponse> getTasksByStatus(@RequestParam List<String> statuses) {
        List<TaskDTO> tasks = taskService.getTaskByStatus(statuses);
        return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(), HttpStatus.OK, "Tasks fetched by status", tasks));
    }
}

