package com.yash.kubesentry.exception;

public class IncidentNotFoundException extends RuntimeException {

    public IncidentNotFoundException(Long id) {
        super("Incident with id " + id + " not found.");
    }
}