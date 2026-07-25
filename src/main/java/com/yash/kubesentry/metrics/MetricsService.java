package com.yash.kubesentry.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

@Service
public class MetricsService {
    private final Counter alertsCreatedCounter;
    private final Counter criticalAlertsCounter;

    public MetricsService(MeterRegistry meterRegistry) {
        this.alertsCreatedCounter = meterRegistry.counter("alerts_created_total");
        this.criticalAlertsCounter = meterRegistry.counter("critical_alerts_total");
    }

    public void alertCreated() {
        alertsCreatedCounter.increment();
    }

    public void criticalAlertCreated() {
        criticalAlertsCounter.increment();
    }
}
