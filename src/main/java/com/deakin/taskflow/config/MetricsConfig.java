package com.deakin.taskflow.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {

    @Bean
    public Counter taskCreatedCounter(MeterRegistry registry) {
        return Counter.builder("taskflow_task_operations_total")
                .tag("operation", "create")
                .description("Total number of created tasks")
                .register(registry);
    }

    @Bean
    public Counter taskReadCounter(MeterRegistry registry) {
        return Counter.builder("taskflow_task_operations_total")
                .tag("operation", "read")
                .description("Total number of read tasks")
                .register(registry);
    }

    @Bean
    public Counter taskUpdatedCounter(MeterRegistry registry) {
        return Counter.builder("taskflow_task_operations_total")
                .tag("operation", "update")
                .description("Total number of updated tasks")
                .register(registry);
    }

    @Bean
    public Counter taskDeletedCounter(MeterRegistry registry) {
        return Counter.builder("taskflow_task_operations_total")
                .tag("operation", "delete")
                .description("Total number of deleted tasks")
                .register(registry);
    }
}
