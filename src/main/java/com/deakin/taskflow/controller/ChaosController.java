package com.deakin.taskflow.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
public class ChaosController {

    @GetMapping("/api/chaos/error")
    public ResponseEntity<Map<String, Object>> simulateIncident(@RequestParam(defaultValue = "500") String type) {
        if ("500".equals(type)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "code", 500,
                    "message", "SIMULATED_INCIDENT: Synthetic 500 error for Prometheus alert rule testing",
                    "timestamp", Instant.now().toString()
            ));
        }

        if ("latency".equals(type)) {
            try {
                Thread.sleep(2500);
            } catch (InterruptedException ignored) {}
            return ResponseEntity.ok(Map.of(
                    "status", "delayed",
                    "delayMs", 2500,
                    "message", "SIMULATED_INCIDENT: Simulated latency spike",
                    "timestamp", Instant.now().toString()
            ));
        }

        return ResponseEntity.badRequest().body(Map.of("error", "Supported types: 500, latency"));
    }

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> rootDiscovery() {
        return ResponseEntity.ok(Map.of(
                "service", "TaskFlow REST API (Java Spring Boot)",
                "status", "online",
                "endpoints", Map.of(
                        "health", "/actuator/health",
                        "metrics", "/actuator/prometheus",
                        "auth", "/api/auth/login",
                        "tasks", "/api/tasks"
                )
        ));
    }
}
