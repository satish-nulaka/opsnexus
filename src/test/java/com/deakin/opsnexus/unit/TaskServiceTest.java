package com.deakin.opsnexus.unit;

import com.deakin.opsnexus.model.Task;
import com.deakin.opsnexus.service.TaskService;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class TaskServiceTest {

    private TaskService taskService;
    private SimpleMeterRegistry meterRegistry;

    @BeforeEach
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();
        taskService = new TaskService(
                meterRegistry.counter("task_created"),
                meterRegistry.counter("task_read"),
                meterRegistry.counter("task_updated"),
                meterRegistry.counter("task_deleted")
        );
    }

    @Test
    void testGetAllTasksWithDefaults() {
        List<Task> tasks = taskService.getAllTasks(null, null);
        assertFalse(tasks.isEmpty());
        assertTrue(tasks.size() >= 2);
    }

    @Test
    void testFilterTasksByPriority() {
        List<Task> highPriority = taskService.getAllTasks(null, "high");
        assertFalse(highPriority.isEmpty());
        for (Task t : highPriority) {
            assertEquals("high", t.getPriority().toLowerCase());
        }
    }

    @Test
    void testCreateTaskIncrementsCounter() {
        double initialCount = meterRegistry.counter("task_created").count();
        Task newTask = new Task(null, "Unit Test Task", "Test Description", "pending", "low", null);
        Task created = taskService.createTask(newTask, "tester");

        assertNotNull(created.getId());
        assertEquals("Unit Test Task", created.getTitle());
        assertEquals("tester", created.getAssignedTo());
        assertEquals(initialCount + 1, meterRegistry.counter("task_created").count());
    }

    @Test
    void testUpdateTask() {
        Task update = new Task();
        update.setStatus("completed");
        Optional<Task> result = taskService.updateTask("task-1", update);

        assertTrue(result.isPresent());
        assertEquals("completed", result.get().getStatus());
    }

    @Test
    void testDeleteTask() {
        boolean deleted = taskService.deleteTask("task-2");
        assertTrue(deleted);
        Optional<Task> fetched = taskService.getTaskById("task-2");
        assertTrue(fetched.isEmpty());
    }
}
