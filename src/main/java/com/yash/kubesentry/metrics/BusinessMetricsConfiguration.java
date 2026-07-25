package com.yash.kubesentry.metrics;

import com.yash.kubesentry.model.enums.IncidentStatus;
import com.yash.kubesentry.model.enums.Severity;
import com.yash.kubesentry.repository.IncidentRepository;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

@Configuration
public class BusinessMetricsConfiguration {

    private final MeterRegistry registry;
    private final IncidentRepository repository;

    public BusinessMetricsConfiguration(MeterRegistry registry,
                                        IncidentRepository repository) {
        this.registry = registry;
        this.repository = repository;
    }

    @PostConstruct
    public void registerGauges() {

        Gauge.builder("kubesentry.incidents.total",
                        repository,
                        IncidentRepository::count)
                .description("Total incidents")
                .register(registry);

        Gauge.builder("kubesentry.incidents.open",
                        repository,
                        repo -> repo.countByStatus(IncidentStatus.OPEN))
                .description("Open incidents")
                .register(registry);

        Gauge.builder("kubesentry.incidents.assigned",
                        repository,
                        repo -> repo.countByStatus(IncidentStatus.ASSIGNED))
                .description("Assigned incidents")
                .register(registry);

        Gauge.builder("kubesentry.incidents.in_progress",
                        repository,
                        repo -> repo.countByStatus(IncidentStatus.IN_PROGRESS))
                .description("In Progress incidents")
                .register(registry);

        Gauge.builder("kubesentry.incidents.resolved",
                        repository,
                        repo -> repo.countByStatus(IncidentStatus.RESOLVED))
                .description("Resolved incidents")
                .register(registry);

        Gauge.builder("kubesentry.incidents.closed",
                        repository,
                        repo -> repo.countByStatus(IncidentStatus.CLOSED))
                .description("Closed incidents")
                .register(registry);

        Gauge.builder("kubesentry.incidents.critical",
                        repository,
                        repo -> repo.countBySeverity(Severity.CRITICAL))
                .description("Critical incidents")
                .register(registry);

        Gauge.builder("kubesentry.incidents.high",
                        repository,
                        repo -> repo.countBySeverity(Severity.HIGH))
                .description("High severity incidents")
                .register(registry);

        Gauge.builder("kubesentry.incidents.medium",
                        repository,
                        repo -> repo.countBySeverity(Severity.MEDIUM))
                .description("Medium severity incidents")
                .register(registry);

        Gauge.builder("kubesentry.incidents.low",
                        repository,
                        repo -> repo.countBySeverity(Severity.LOW))
                .description("Low severity incidents")
                .register(registry);
    }
}