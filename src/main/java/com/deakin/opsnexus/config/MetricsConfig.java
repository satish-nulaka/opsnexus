package com.deakin.opsnexus.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {

    @Bean
    public Counter taskCreatedCounter(MeterRegistry registry) {
        return Counter.builder("opsnexus_task_operations_total")
                .tag("operation", "create")
                .description("Total number of created tasks in OpsNexus")
                .register(registry);
    }

    @Bean
    public Counter taskReadCounter(MeterRegistry registry) {
        return Counter.builder("opsnexus_task_operations_total")
                .tag("operation", "read")
                .description("Total number of read tasks in OpsNexus")
                .register(registry);
    }

    @Bean
    public Counter taskUpdatedCounter(MeterRegistry registry) {
        return Counter.builder("opsnexus_task_operations_total")
                .tag("operation", "update")
                .description("Total number of updated tasks in OpsNexus")
                .register(registry);
    }

    @Bean
    public Counter taskDeletedCounter(MeterRegistry registry) {
        return Counter.builder("opsnexus_task_operations_total")
                .tag("operation", "delete")
                .description("Total number of deleted tasks in OpsNexus")
                .register(registry);
    }
}
