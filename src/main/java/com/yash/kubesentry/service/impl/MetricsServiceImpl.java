package com.yash.kubesentry.service.impl;

import com.yash.kubesentry.service.MetricsService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

@Service
public class MetricsServiceImpl implements MetricsService {
    private final Counter incidentCreatedCounter;
    private final Counter incidentDeletedCounter;
    public MetricsServiceImpl(MeterRegistry registry) {
        System.out.println("MetricsService initialized");
        System.out.println("Registry class: " + registry.getClass().getName());
        System.out.println("Registry identity: " + System.identityHashCode(registry));
        incidentCreatedCounter = Counter.builder("kubesentry.incidents.created")
                .description("Total incidents created")
                .register(registry);
        incidentDeletedCounter = Counter.builder("kubesentry.incidents.deleted")
                .description("Total incidents deleted")
                .register(registry);

        System.out.println(registry.getClass().getName());
        System.out.println(System.identityHashCode(registry));
    }
    @Override
    public void incrementIncidentCreated() {
        System.out.println("Incrementing incident counter");
        incidentCreatedCounter.increment();
        System.out.println("Counter = " + incidentCreatedCounter.count());
    }
    @Override
    public void incrementIncidentDeleted() {
        incidentDeletedCounter.increment();
    }
}
