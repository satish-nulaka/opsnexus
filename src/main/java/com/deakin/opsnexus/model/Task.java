package com.deakin.opsnexus.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.Instant;

@Schema(description = "OpsNexus Task Entity representing a workflow unit")
public class Task {

    @Schema(description = "Unique identifier of the task", example = "task-1")
    private String id;

    @Schema(description = "Title of the task", example = "Automate Jenkins CI/CD Pipeline", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Title cannot be blank")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;

    @Schema(description = "Detailed task description", example = "Implement all 7 automated delivery stages", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Description cannot be blank")
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @Schema(description = "Current lifecycle status", example = "in-progress", allowableValues = {"pending", "in-progress", "completed"})
    private String status;

    @Schema(description = "Urgency priority level", example = "high", allowableValues = {"low", "medium", "high", "critical"})
    private String priority;

    @Schema(description = "Username of assigned owner", example = "devops_lead")
    private String assignedTo;

    @Schema(description = "Task creation timestamp in ISO-8601 format", example = "2026-11-17T12:00:00Z")
    private Instant createdAt;

    @Schema(description = "Last update timestamp in ISO-8601 format", example = "2026-11-17T12:30:00Z")
    private Instant updatedAt;

    public Task() {
        this.createdAt = Instant.now();
        this.status = "pending";
        this.priority = "medium";
    }

    public Task(String id, String title, String description, String status, String priority, String assignedTo) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status != null ? status : "pending";
        this.priority = priority != null ? priority : "medium";
        this.assignedTo = assignedTo;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getAssignedTo() { return assignedTo; }
    public void setAssignedTo(String assignedTo) { this.assignedTo = assignedTo; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
