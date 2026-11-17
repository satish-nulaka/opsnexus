package com.deakin.taskflow.service;

import com.deakin.taskflow.model.Task;
import io.micrometer.core.instrument.Counter;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TaskService {

    private final Map<String, Task> taskRepository = new ConcurrentHashMap<>();
    private final Counter taskCreatedCounter;
    private final Counter taskReadCounter;
    private final Counter taskUpdatedCounter;
    private final Counter taskDeletedCounter;

    public TaskService(Counter taskCreatedCounter,
                       Counter taskReadCounter,
                       Counter taskUpdatedCounter,
                       Counter taskDeletedCounter) {
        this.taskCreatedCounter = taskCreatedCounter;
        this.taskReadCounter = taskReadCounter;
        this.taskUpdatedCounter = taskUpdatedCounter;
        this.taskDeletedCounter = taskDeletedCounter;

        initDefaultTasks();
    }

    public void initDefaultTasks() {
        taskRepository.clear();
        Task t1 = new Task("task-1", "Configure Jenkins Pipeline",
                "Automate all 7 CI/CD stages with Maven, Docker, and Prometheus",
                "in-progress", "high", "devops_lead");
        Task t2 = new Task("task-2", "Integrate SonarQube Quality Gates",
                "Set strict coverage and code maintainability thresholds",
                "completed", "high", "devops_lead");
        taskRepository.put(t1.getId(), t1);
        taskRepository.put(t2.getId(), t2);
    }

    public List<Task> getAllTasks(String status, String priority) {
        taskReadCounter.increment();
        List<Task> result = new ArrayList<>(taskRepository.values());
        if (status != null && !status.isBlank()) {
            result.removeIf(t -> !t.getStatus().equalsIgnoreCase(status));
        }
        if (priority != null && !priority.isBlank()) {
            result.removeIf(t -> !t.getPriority().equalsIgnoreCase(priority));
        }
        return result;
    }

    public Optional<Task> getTaskById(String id) {
        taskReadCounter.increment();
        return Optional.ofNullable(taskRepository.get(id));
    }

    public Task createTask(Task task, String username) {
        String id = "task-" + UUID.randomUUID().toString().substring(0, 8);
        task.setId(id);
        task.setAssignedTo(username != null ? username : "unassigned");
        task.setCreatedAt(Instant.now());
        task.setUpdatedAt(Instant.now());
        taskRepository.put(id, task);
        taskCreatedCounter.increment();
        return task;
    }

    public Optional<Task> updateTask(String id, Task updated) {
        Task existing = taskRepository.get(id);
        if (existing == null) {
            return Optional.empty();
        }
        if (updated.getTitle() != null) existing.setTitle(updated.getTitle());
        if (updated.getDescription() != null) existing.setDescription(updated.getDescription());
        if (updated.getStatus() != null) existing.setStatus(updated.getStatus());
        if (updated.getPriority() != null) existing.setPriority(updated.getPriority());
        existing.setUpdatedAt(Instant.now());

        taskUpdatedCounter.increment();
        return Optional.of(existing);
    }

    public boolean deleteTask(String id) {
        Task removed = taskRepository.remove(id);
        if (removed != null) {
            taskDeletedCounter.increment();
            return true;
        }
        return false;
    }
}
