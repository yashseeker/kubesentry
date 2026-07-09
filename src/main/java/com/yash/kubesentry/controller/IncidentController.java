package com.yash.kubesentry.controller;
import com.yash.kubesentry.dto.PageResponseDTO;
import com.yash.kubesentry.model.enums.IncidentStatus;
import com.yash.kubesentry.dto.IncidentRequestDTO;
import com.yash.kubesentry.dto.IncidentResponseDTO;
import com.yash.kubesentry.dto.UpdateIncidentStatusRequest;
import com.yash.kubesentry.model.enums.Severity;
import com.yash.kubesentry.service.IncidentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.data.domain.Page;
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
    public ResponseEntity<ApiResponse<PageResponseDTO<IncidentResponseDTO>>>getAllIncidents(

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            @RequestParam(defaultValue = "detectedAt")
            String sortBy,

            @RequestParam(defaultValue = "desc")
            String sortDir,

            @RequestParam(required = false)
                    IncidentStatus status,

            @RequestParam(required = false)
            Severity severity,

            @RequestParam(required = false)
                    String search
    ) {

        PageResponseDTO<IncidentResponseDTO> incidents =
                incidentService.getAllIncidents(
                        page,
                        size,
                        sortBy,
                        sortDir,
                        status,
                        severity,
                        search
                );

        ApiResponse<PageResponseDTO<IncidentResponseDTO>> response =
                ApiResponse.<PageResponseDTO<IncidentResponseDTO>>builder()
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
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<IncidentResponseDTO>> updateIncidentStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateIncidentStatusRequest request) {

        IncidentResponseDTO response =
                incidentService.updateIncidentStatus(id, request.getStatus());

        ApiResponse<IncidentResponseDTO> apiResponse =
                ApiResponse.<IncidentResponseDTO>builder()
                        .success(true)
                        .message("Incident status updated successfully")
                        .data(response)
                        .build();

        return ResponseEntity.ok(apiResponse);
    }
}