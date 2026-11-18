package com.deakin.opsnexus.controller;

import com.deakin.opsnexus.model.Task;
import com.deakin.opsnexus.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Task & Workflow Management", description = "CRUD operations for workflow tasks with RBAC security")
@SecurityRequirement(name = "Bearer Authentication")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    @Operation(summary = "List all workflow tasks", description = "Retrieves a filtered or complete collection of tasks.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Missing or invalid JWT token")
    })
    public ResponseEntity<Map<String, Object>> getAllTasks(
            @Parameter(description = "Filter by status (pending, in-progress, completed)") @RequestParam(required = false) String status,
            @Parameter(description = "Filter by priority (low, medium, high, critical)") @RequestParam(required = false) String priority) {
        List<Task> tasks = taskService.getAllTasks(status, priority);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "count", tasks.size(),
                "data", tasks
        ));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get task by ID", description = "Returns a single task matching the provided identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task found"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    public ResponseEntity<?> getTaskById(
            @Parameter(description = "Unique ID of the task") @PathVariable String id) {
        return taskService.getTaskById(id)
                .<ResponseEntity<?>>map(task -> ResponseEntity.ok(Map.of("success", true, "data", task)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("success", false, "error", "Task with id '" + id + "' not found")));
    }

    @PostMapping
    @Operation(summary = "Create a new workflow task", description = "Creates a new task assigned to the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid payload / validation failure")
    })
    public ResponseEntity<?> createTask(@Valid @RequestBody Task task, Authentication auth) {
        String username = auth != null ? auth.getName() : "user";
        Task created = taskService.createTask(task, username);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("success", true, "message", "Task created successfully", "data", created));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing task", description = "Modifies status, priority, or content of a task.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task updated successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    public ResponseEntity<?> updateTask(
            @Parameter(description = "Task ID") @PathVariable String id,
            @RequestBody Task task) {
        return taskService.updateTask(id, task)
                .<ResponseEntity<?>>map(updated -> ResponseEntity.ok(Map.of("success", true, "message", "Task updated successfully", "data", updated)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("success", false, "error", "Task with id '" + id + "' not found")));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete task", description = "Removes a task from the system permanently.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    public ResponseEntity<?> deleteTask(
            @Parameter(description = "Task ID") @PathVariable String id) {
        boolean deleted = taskService.deleteTask(id);
        if (deleted) {
            return ResponseEntity.ok(Map.of("success", true, "message", "Task deleted successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("success", false, "error", "Task with id '" + id + "' not found"));
        }
    }
}
