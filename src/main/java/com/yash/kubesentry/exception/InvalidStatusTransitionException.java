package com.yash.kubesentry.exception;

import com.yash.kubesentry.model.enums.IncidentStatus;

public class InvalidStatusTransitionException extends RuntimeException {

    public InvalidStatusTransitionException(
            IncidentStatus current,
            IncidentStatus next) {

        super("Invalid status transition from "
                + current
                + " to "
                + next);

    }

}