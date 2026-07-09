package com.yash.kubesentry.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yash.kubesentry.dto.IncidentRequestDTO;
import com.yash.kubesentry.dto.IncidentResponseDTO;
import com.yash.kubesentry.dto.PageResponseDTO;
import com.yash.kubesentry.dto.UpdateIncidentStatusRequest;
import com.yash.kubesentry.exception.IncidentNotFoundException;
import com.yash.kubesentry.exception.InvalidStatusTransitionException;
import com.yash.kubesentry.model.enums.IncidentStatus;
import com.yash.kubesentry.model.enums.Severity;
import com.yash.kubesentry.service.IncidentService;

import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(IncidentController.class)
class IncidentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IncidentService incidentService;

    @Test
    void shouldCreateIncidentSuccessfully() throws Exception {

        IncidentRequestDTO request =
                new IncidentRequestDTO();

        request.setTitle("Database Down");
        request.setDescription("Postgres crashed");
        request.setSeverity(Severity.HIGH);
        request.setNamespace("production");
        request.setPodName("postgres-0");
        request.setNodeName("worker-1");

        IncidentResponseDTO response =
                IncidentResponseDTO.builder()
                        .id(1L)
                        .title("Database Down")
                        .description("Postgres crashed")
                        .namespace("production")
                        .podName("postgres-0")
                        .nodeName("worker-1")
                        .severity(Severity.HIGH)
                        .status(IncidentStatus.OPEN)
                        .build();

        when(incidentService.saveIncident(any()))
                .thenReturn(response);

        mockMvc.perform(
                        post("/api/incidents")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )

                .andExpect(status().isCreated())

                .andExpect(jsonPath("$.success")
                        .value(true))

                .andExpect(jsonPath("$.data.title")
                        .value("Database Down"));
    }

    @Test
    void shouldGetIncidentByIdSuccessfully() throws Exception {

        IncidentResponseDTO response =
                IncidentResponseDTO.builder()
                        .id(1L)
                        .title("Database Down")
                        .description("Postgres crashed")
                        .namespace("production")
                        .podName("postgres-0")
                        .nodeName("worker-1")
                        .severity(Severity.HIGH)
                        .status(IncidentStatus.OPEN)
                        .detectedAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build();

        when(incidentService.getIncidentById(1L))
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/incidents/{id}", 1L)
                )

                .andExpect(status().isOk())

                .andExpect(jsonPath("$.success")
                        .value(true))

                .andExpect(jsonPath("$.data.id")
                        .value(1))

                .andExpect(jsonPath("$.data.title")
                        .value("Database Down"))

                .andExpect(jsonPath("$.data.status")
                        .value("OPEN"));
    }

    @Test
    void shouldGetAllIncidents() throws Exception{
        IncidentResponseDTO incident =
                IncidentResponseDTO.builder()
                        .id(1L)
                        .title("Database Down")
                        .description("Postgres crashed")
                        .namespace("production")
                        .podName("postgres-0")
                        .nodeName("worker-1")
                        .severity(Severity.HIGH)
                        .status(IncidentStatus.OPEN)
                        .detectedAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build();

        PageResponseDTO<IncidentResponseDTO> pageResponse =
                PageResponseDTO.<IncidentResponseDTO>builder()
                        .content(List.of(incident))
                        .page(0)
                        .size(10)
                        .totalElements(1)
                        .totalPages(1)
                        .first(true)
                        .last(true)
                        .empty(false)
                        .build();

        when(
                incidentService.getAllIncidents(
                        anyInt(),
                        anyInt(),
                        anyString(),
                        anyString(),
                        any(),
                        any(),
                        any()
                )
        ).thenReturn(pageResponse);

        mockMvc.perform(

                get("/api/incidents")

        ).andExpect(status().isOk())

                .andExpect(jsonPath("$.success")
                        .value(true))

                .andExpect(jsonPath("$.data.content[0].title")
                        .value("Database Down"))

                .andExpect(jsonPath("$.data.totalElements")
                        .value(1))

                .andExpect(jsonPath("$.data.page")
                        .value(0));
    }

    @Test
    void shouldUpdateIncidentStatusSuccessfully() throws Exception {

        IncidentResponseDTO response =
                IncidentResponseDTO.builder()
                        .id(1L)
                        .title("Database Down")
                        .description("Postgres crashed")
                        .namespace("production")
                        .podName("postgres-0")
                        .nodeName("worker-1")
                        .severity(Severity.HIGH)
                        .status(IncidentStatus.IN_PROGRESS)
                        .detectedAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build();

        UpdateIncidentStatusRequest request =
                new UpdateIncidentStatusRequest();

        request.setStatus(IncidentStatus.IN_PROGRESS);

        when(
                incidentService.updateIncidentStatus(
                        1L,
                        IncidentStatus.IN_PROGRESS
                )
        ).thenReturn(response);

        mockMvc.perform(

                        patch("/api/incidents/{id}/status", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )

                )

                .andExpect(status().isOk())

                .andExpect(jsonPath("$.success")
                        .value(true))

                .andExpect(jsonPath("$.data.status")
                        .value("IN_PROGRESS"));
    }

    @Test
    void shouldDeleteIncidentSuccessfully() throws Exception {

        doNothing()

                .when(incidentService)
                .deleteIncident(1L);
        mockMvc.perform(
                        delete("/api/incidents/{id}",1L)
                )
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.success")
                        .value(true))

                .andExpect(jsonPath("$.message")
                        .value("Incident deleted successfully"));
    }

    @Test
    void shouldReturnBadRequestWhenRequestIsInvalid() throws Exception {

        IncidentRequestDTO request =
                new IncidentRequestDTO();

        request.setDescription("Postgres crashed");

        mockMvc.perform(

                        post("/api/incidents")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )

                )

                .andExpect(status().isBadRequest())

                .andExpect(jsonPath("$.title")
                        .exists())

                .andExpect(jsonPath("$.namespace")
                        .exists())

                .andExpect(jsonPath("$.podName")
                        .exists())

                .andExpect(jsonPath("$.nodeName")
                        .exists())

                .andExpect(jsonPath("$.severity")
                        .exists());

        verifyNoInteractions(incidentService);
    }

    @Test
    void shouldReturnNotFoundWhenIncidentDoesNotExist() throws Exception {

        when(
                incidentService.getIncidentById(999L)
        ).thenThrow(
                new IncidentNotFoundException(999L)
        );

        mockMvc.perform(

                        get("/api/incidents/{id}", 999L)

                )

                .andExpect(status().isNotFound())

                .andExpect(jsonPath("$.status")
                        .value(404))

                .andExpect(jsonPath("$.message")
                        .value("Incident with id 999 not found."));
    }

    @Test
    void shouldReturnNotFoundWhenDeletingUnknownIncident() throws Exception {

        doThrow(
                new IncidentNotFoundException(999L)
        )

                .when(incidentService)

                .deleteIncident(999L);

        mockMvc.perform(

                        delete("/api/incidents/{id}", 999L)

                )

                .andExpect(status().isNotFound())

                .andExpect(jsonPath("$.status")
                        .value(404))

                .andExpect(jsonPath("$.message")
                        .value("Incident with id 999 not found."));
    }

    @Test
    void shouldReturnBadRequestForInvalidStatusTransition() throws Exception {

        UpdateIncidentStatusRequest request =
                new UpdateIncidentStatusRequest();

        request.setStatus(IncidentStatus.OPEN);

        when(
                incidentService.updateIncidentStatus(
                        1L,
                        IncidentStatus.OPEN
                )
        ).thenThrow(
                new InvalidStatusTransitionException(
                        IncidentStatus.CLOSED,
                        IncidentStatus.OPEN
                )
        );

        mockMvc.perform(

                        patch("/api/incidents/{id}/status", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )

                )

                .andExpect(status().isBadRequest())

                .andExpect(jsonPath("$.success")
                        .value(false))

                .andExpect(jsonPath("$.message")
                        .value("Invalid status transition from CLOSED to OPEN"));
    }

    @Test
    void shouldReturnBadRequestWhenRequestBodyIsMissing() throws Exception {

        mockMvc.perform(

                        patch("/api/incidents/{id}/status", 1L)
                                .contentType(MediaType.APPLICATION_JSON)

                )

                .andExpect(status().isBadRequest());

        verifyNoInteractions(incidentService);
    }

    @Test
    void shouldReturnBadRequestForInvalidSeverityEnum() throws Exception {

        String invalidJson = """
            {
                "title":"Database Down",
                "description":"Postgres crashed",
                "namespace":"production",
                "podName":"postgres-0",
                "nodeName":"worker-1",
                "severity":"SUPER_HIGH"
            }
            """;

        mockMvc.perform(

                        post("/api/incidents")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(invalidJson)

                )

                .andExpect(status().isBadRequest());

        verifyNoInteractions(incidentService);
    }
}
