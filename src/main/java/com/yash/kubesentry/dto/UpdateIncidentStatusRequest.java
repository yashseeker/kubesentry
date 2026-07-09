package com.yash.kubesentry.dto;

import com.yash.kubesentry.model.enums.IncidentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateIncidentStatusRequest {

    @NotNull(message = "Status is required")
    private IncidentStatus status;

}