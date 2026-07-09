package com.yash.kubesentry.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yash.kubesentry.dto.IncidentRequestDTO;
import com.yash.kubesentry.dto.UpdateIncidentStatusRequest;
import com.yash.kubesentry.model.entity.Incident;
import com.yash.kubesentry.model.enums.IncidentStatus;
import com.yash.kubesentry.model.enums.Severity;
import com.yash.kubesentry.repository.IncidentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class IncidentIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IncidentRepository incidentRepository;

    @Test
    void shouldCreateIncidentSuccessfully() throws Exception {

        IncidentRequestDTO request = new IncidentRequestDTO();

        request.setTitle("Database Down");
        request.setDescription("Postgres crashed");
        request.setNamespace("production");
        request.setPodName("postgres-0");
        request.setNodeName("worker-1");
        request.setSeverity(Severity.HIGH);

        mockMvc.perform(
                        post("/api/incidents")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.title").value("Database Down"));

        assertEquals(1, incidentRepository.count());
    }

    @Test
    void shouldGetIncidentByIdSuccessfully() throws Exception {

        Incident incident = incidentRepository.save(
                Incident.builder()
                        .title("Database Down")
                        .description("Postgres crashed")
                        .namespace("production")
                        .podName("postgres-0")
                        .nodeName("worker-1")
                        .severity(Severity.HIGH)
                        .status(IncidentStatus.OPEN)
                        .build()
        );

        mockMvc.perform(
                        get("/api/incidents/{id}", incident.getId())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(incident.getId()))
                .andExpect(jsonPath("$.data.title").value("Database Down"));
    }

    @Test
    void shouldGetAllIncidentsSuccessfully() throws Exception {

        incidentRepository.save(
                Incident.builder()
                        .title("DB Down")
                        .description("desc")
                        .namespace("production")
                        .podName("pod-1")
                        .nodeName("worker-1")
                        .severity(Severity.HIGH)
                        .status(IncidentStatus.OPEN)
                        .build()
        );

        incidentRepository.save(
                Incident.builder()
                        .title("CPU High")
                        .description("desc")
                        .namespace("production")
                        .podName("pod-2")
                        .nodeName("worker-2")
                        .severity(Severity.MEDIUM)
                        .status(IncidentStatus.OPEN)
                        .build()
        );

        mockMvc.perform(get("/api/incidents"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content.length()").value(2));
    }

    @Test
    void shouldUpdateIncidentStatusSuccessfully() throws Exception {

        Incident incident = incidentRepository.save(
                Incident.builder()
                        .title("Database Down")
                        .description("Postgres crashed")
                        .namespace("production")
                        .podName("postgres-0")
                        .nodeName("worker-1")
                        .severity(Severity.HIGH)
                        .status(IncidentStatus.OPEN)
                        .build()
        );

        UpdateIncidentStatusRequest request =
                new UpdateIncidentStatusRequest();

        request.setStatus(IncidentStatus.ASSIGNED);

        mockMvc.perform(
                        patch("/api/incidents/{id}/status", incident.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("ASSIGNED"));

        Incident updated =
                incidentRepository.findById(incident.getId()).orElseThrow();

        assertEquals(
                IncidentStatus.ASSIGNED,
                updated.getStatus()
        );
    }

    @Test
    void shouldDeleteIncidentSuccessfully() throws Exception {

        Incident incident = incidentRepository.save(
                Incident.builder()
                        .title("Delete Me")
                        .description("desc")
                        .namespace("production")
                        .podName("pod")
                        .nodeName("worker")
                        .severity(Severity.LOW)
                        .status(IncidentStatus.OPEN)
                        .build()
        );

        mockMvc.perform(
                        delete("/api/incidents/{id}", incident.getId())
                )
                .andDo(print())
                .andExpect(status().isOk());

        assertFalse(
                incidentRepository.findById(
                        incident.getId()
                ).isPresent()
        );
    }

    @Test
    void shouldReturnBadRequestForInvalidRequest() throws Exception {

        IncidentRequestDTO request =
                new IncidentRequestDTO();

        mockMvc.perform(

                        post("/api/incidents")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )

                )

                .andDo(print())

                .andExpect(status().isBadRequest())

                .andExpect(jsonPath("$.title").exists())

                .andExpect(jsonPath("$.description").exists())

                .andExpect(jsonPath("$.namespace").exists())

                .andExpect(jsonPath("$.podName").exists())

                .andExpect(jsonPath("$.nodeName").exists())

                .andExpect(jsonPath("$.severity").exists());
    }

    @Test
    void shouldReturnNotFoundWhenIncidentDoesNotExist() throws Exception {

        mockMvc.perform(

                        get("/api/incidents/{id}",999L)

                )

                .andDo(print())

                .andExpect(status().isNotFound())

                .andExpect(jsonPath("$.message")
                        .value("Incident with id 999 not found."));
    }

    @Test
    void shouldReturnBadRequestForInvalidStatusTransition() throws Exception {

        Incident incident =
                incidentRepository.save(

                        Incident.builder()
                                .title("Database Down")
                                .description("Postgres crashed")
                                .namespace("production")
                                .podName("postgres-0")
                                .nodeName("worker-1")
                                .severity(Severity.HIGH)
                                .status(IncidentStatus.OPEN)
                                .build()

                );

        UpdateIncidentStatusRequest request =
                new UpdateIncidentStatusRequest();

        request.setStatus(IncidentStatus.CLOSED);

        mockMvc.perform(

                        patch(
                                "/api/incidents/{id}/status",
                                incident.getId()
                        )

                                .contentType(MediaType.APPLICATION_JSON)

                                .content(
                                        objectMapper.writeValueAsString(request)
                                )

                )

                .andDo(print())

                .andExpect(status().isBadRequest())

                .andExpect(jsonPath("$.success")
                        .value(false))

                .andExpect(jsonPath("$.message")
                        .exists());
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistingIncident() throws Exception {

        mockMvc.perform(

                        delete("/api/incidents/{id}",999L)

                )

                .andDo(print())

                .andExpect(status().isNotFound())

                .andExpect(jsonPath("$.message")
                        .value("Incident with id 999 not found."));
    }



}