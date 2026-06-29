package com.yash.kubesentry.dto;

import com.yash.kubesentry.model.enums.Severity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class IncidentRequestDTO {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Namespace is required")
    private String namespace;

    @NotBlank(message = "Pod name is required")
    private String podName;

    @NotBlank(message = "Node name is required")
    private String nodeName;

    @NotNull(message = "Severity is required")
    private Severity severity;
}