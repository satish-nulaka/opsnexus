package com.deakin.taskflow.controller;

import com.deakin.taskflow.model.Task;
import com.deakin.taskflow.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllTasks(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority) {
        List<Task> tasks = taskService.getAllTasks(status, priority);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "count", tasks.size(),
                "data", tasks
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTaskById(@PathVariable String id) {
        return taskService.getTaskById(id)
                .<ResponseEntity<?>>map(task -> ResponseEntity.ok(Map.of("success", true, "data", task)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("success", false, "error", "Task with id '" + id + "' not found")));
    }

    @PostMapping
    public ResponseEntity<?> createTask(@Valid @RequestBody Task task, Authentication auth) {
        String username = auth != null ? auth.getName() : "user";
        Task created = taskService.createTask(task, username);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("success", true, "message", "Task created successfully", "data", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable String id, @RequestBody Task task) {
        return taskService.updateTask(id, task)
                .<ResponseEntity<?>>map(updated -> ResponseEntity.ok(Map.of("success", true, "message", "Task updated successfully", "data", updated)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("success", false, "error", "Task with id '" + id + "' not found")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable String id) {
        boolean deleted = taskService.deleteTask(id);
        if (deleted) {
            return ResponseEntity.ok(Map.of("success", true, "message", "Task deleted successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("success", false, "error", "Task with id '" + id + "' not found"));
        }
    }
}
