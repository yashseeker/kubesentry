package com.yash.kubesentry.util;

import com.yash.kubesentry.exception.InvalidStatusTransitionException;
import com.yash.kubesentry.model.enums.IncidentStatus;

import java.util.Map;
import java.util.Set;

public class StatusTransitionValidator {

    private static final Map<IncidentStatus, Set<IncidentStatus>> VALID_TRANSITIONS =
            Map.of(
                    IncidentStatus.OPEN, Set.of(IncidentStatus.ASSIGNED),
                    IncidentStatus.ASSIGNED, Set.of(IncidentStatus.IN_PROGRESS),
                    IncidentStatus.IN_PROGRESS, Set.of(IncidentStatus.RESOLVED),
                    IncidentStatus.RESOLVED, Set.of(IncidentStatus.CLOSED),
                    IncidentStatus.CLOSED, Set.of()
            );

    public static void validate(IncidentStatus current, IncidentStatus next) {

        if (!VALID_TRANSITIONS.get(current).contains(next)) {

            throw new InvalidStatusTransitionException(current, next);

        }


    }

}