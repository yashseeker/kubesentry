package com.yash.kubesentry.dto;

import com.yash.kubesentry.model.enums.IncidentStatus;
import com.yash.kubesentry.model.enums.Severity;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class IncidentResponseDTO {

    private Long id;

    private String title;

    private String description;

    private String namespace;

    private String podName;

    private String nodeName;

    private Severity severity;

    private IncidentStatus status;

    private LocalDateTime detectedAt;

    private LocalDateTime updatedAt;
}