package com.yash.kubesentry.service;

public interface MetricsService {
    void incrementIncidentCreated();
    void incrementIncidentDeleted();
}
