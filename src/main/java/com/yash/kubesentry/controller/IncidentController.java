package com.yash.kubesentry.controller;

import com.yash.kubesentry.dto.IncidentRequestDTO;
import com.yash.kubesentry.dto.IncidentResponseDTO;
import com.yash.kubesentry.service.IncidentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.yash.kubesentry.payload.ApiResponse;
import java.util.List;

@RestController
@RequestMapping("/api/incidents")
public class IncidentController {
    private final IncidentService incidentService;
    public IncidentController(IncidentService incidentService) {
        this.incidentService = incidentService;
    }
    @PostMapping
    public ResponseEntity<ApiResponse<IncidentResponseDTO>> createIncident(
            @Valid @RequestBody IncidentRequestDTO requestDTO) {
        IncidentResponseDTO response = incidentService.saveIncident(requestDTO);

        ApiResponse<IncidentResponseDTO> apiResponse =
                ApiResponse.<IncidentResponseDTO>builder()
                        .success(true)
                        .message("Incident created successfully")
                        .data(response)
                        .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }
    @GetMapping
    public ResponseEntity<ApiResponse<List<IncidentResponseDTO>>> getAllIncidents() {

        List<IncidentResponseDTO> incidents =
                incidentService.getAllIncidents();

        ApiResponse<List<IncidentResponseDTO>> response =
                ApiResponse.<List<IncidentResponseDTO>>builder()
                        .success(true)
                        .message("Incidents fetched successfully")
                        .data(incidents)
                        .build();

        return ResponseEntity.ok(response);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<IncidentResponseDTO>> getIncidentById(
            @PathVariable Long id) {

        IncidentResponseDTO incident =
                incidentService.getIncidentById(id);

        ApiResponse<IncidentResponseDTO> response =
                ApiResponse.<IncidentResponseDTO>builder()
                        .success(true)
                        .message("Incident fetched successfully")
                        .data(incident)
                        .build();

        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteIncident(
            @PathVariable Long id) {

        incidentService.deleteIncident(id);

        ApiResponse<Void> response =
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Incident deleted successfully")
                        .data(null)
                        .build();

        return ResponseEntity.ok(response);
    }
}