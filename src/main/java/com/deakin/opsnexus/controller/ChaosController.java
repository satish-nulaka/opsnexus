package com.deakin.opsnexus.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
@Tag(name = "DevOps Chaos & Reliability", description = "Endpoints for simulating incidents to validate Prometheus & Grafana alerting")
public class ChaosController {

    @GetMapping("/api/chaos/error")
    @Operation(summary = "Simulate application incident", description = "Intentionally triggers a 500 error or latency spike to test alert triggers in Stage 7 of the Jenkins pipeline.")
    public ResponseEntity<Map<String, Object>> simulateIncident(
            @Parameter(description = "Chaos type ('500' or 'latency')", example = "500")
            @RequestParam(defaultValue = "500") String type) {
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
    @Operation(summary = "Root API Service Discovery", description = "Provides navigation links to Swagger UI, Health Probes, and Prometheus metrics.")
    public ResponseEntity<Map<String, Object>> rootDiscovery() {
        return ResponseEntity.ok(Map.of(
                "service", "OpsNexus Enterprise Workflow API",
                "version", "1.0.0",
                "status", "online",
                "documentation", Map.of(
                        "swaggerUI", "/swagger-ui.html",
                        "openApiSpec", "/v3/api-docs",
                        "health", "/actuator/health",
                        "metrics", "/actuator/prometheus",
                        "auth", "/api/auth/login",
                        "tasks", "/api/tasks"
                )
        ));
    }
}
