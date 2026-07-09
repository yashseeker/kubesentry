package com.yash.kubesentry.service.impl;
import com.yash.kubesentry.dto.IncidentRequestDTO;
import com.yash.kubesentry.dto.IncidentResponseDTO;
import com.yash.kubesentry.exception.IncidentNotFoundException;
import com.yash.kubesentry.model.entity.Incident;
import com.yash.kubesentry.model.enums.IncidentStatus;
import com.yash.kubesentry.model.enums.Severity;
import com.yash.kubesentry.repository.IncidentRepository;
import java.util.Optional; // for get incident
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.util.List;
import com.yash.kubesentry.dto.PageResponseDTO;
import org.springframework.data.jpa.domain.Specification;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class IncidentServiceImplTest {

    @Mock
    private IncidentRepository incidentRepository;

    @InjectMocks
    private IncidentServiceImpl incidentService;

    @Test
    void shouldSaveIncidentSuccessfully() {

        // Given
        IncidentRequestDTO request = new IncidentRequestDTO();

        request.setTitle("Database Down");
        request.setDescription("Postgres crashed");
        request.setSeverity(Severity.HIGH);
        request.setNamespace("production");
        request.setPodName("postgres-0");
        request.setNodeName("worker-1");

        Incident savedIncident = new Incident();

        savedIncident.setId(1L);
        savedIncident.setTitle(request.getTitle());
        savedIncident.setDescription(request.getDescription());
        savedIncident.setSeverity(request.getSeverity());
        savedIncident.setNamespace(request.getNamespace());
        savedIncident.setPodName(request.getPodName());
        savedIncident.setNodeName(request.getNodeName());
        savedIncident.setStatus(IncidentStatus.OPEN);

        when(incidentRepository.save(any(Incident.class)))
                .thenReturn(savedIncident);

        // When
        IncidentResponseDTO response =
                incidentService.saveIncident(request);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Database Down", response.getTitle());
        assertEquals(IncidentStatus.OPEN, response.getStatus());
        verify(incidentRepository, times(1))
                .save(any(Incident.class));
    }

    @Test
    void shouldGetIncidentByIdSuccessfully() {

        // Given
        Incident incident = new Incident();

        incident.setId(1L);
        incident.setTitle("Database Down");
        incident.setDescription("Postgres crashed");
        incident.setSeverity(Severity.HIGH);
        incident.setStatus(IncidentStatus.OPEN);

        when(incidentRepository.findById(1L))
                .thenReturn(Optional.of(incident));

        // When
        IncidentResponseDTO response =
                incidentService.getIncidentById(1L);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Database Down", response.getTitle());
        assertEquals(
                IncidentStatus.OPEN,
                response.getStatus()
        );
        verify(incidentRepository)
                .findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenIncidentNotFound() {
        // Given
        when(incidentRepository.findById(99L))
                .thenReturn(Optional.empty());
        // When & Then
        IncidentNotFoundException exception =
                assertThrows(
                        IncidentNotFoundException.class,
                        () -> incidentService.getIncidentById(99L)
                );
        assertTrue(
                exception.getMessage().contains("99")
        );
        verify(incidentRepository)
                .findById(99L);
    }

    @Test
    void shouldUpdateIncidentStatusSuccessfully() {

        // Given
        Incident incident = new Incident();
        incident.setId(1L);
        incident.setTitle("Database Down");
        incident.setStatus(IncidentStatus.OPEN);
        incident.setSeverity(Severity.HIGH);

        when(incidentRepository.findById(1L))
                .thenReturn(Optional.of(incident));

        when(incidentRepository.save(any(Incident.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // When
        IncidentResponseDTO response =
                incidentService.updateIncidentStatus(
                        1L,
                        IncidentStatus.ASSIGNED
                );

        // Then
        assertNotNull(response);
        assertEquals(
                IncidentStatus.ASSIGNED,
                response.getStatus()
        );

        verify(incidentRepository)
                .findById(1L);

        ArgumentCaptor<Incident> captor =
                ArgumentCaptor.forClass(Incident.class);

        verify(incidentRepository)
                .save(captor.capture());

        Incident savedIncident = captor.getValue();

        assertEquals(
                IncidentStatus.ASSIGNED,
                savedIncident.getStatus()
        );
    }

    @Test
    void shouldDeleteIncidentSuccessfully() {

        // Given
        Long incidentId = 1L;

        when(incidentRepository.existsById(incidentId))
                .thenReturn(true);

        // When
        incidentService.deleteIncident(incidentId);

        // Then
        verify(incidentRepository)
                .existsById(incidentId);

        verify(incidentRepository)
                .deleteById(incidentId);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingIncident() {

        Long incidentId = 99L;

        when(incidentRepository.existsById(incidentId))
                .thenReturn(false);

        // When + Then
        IncidentNotFoundException exception =
                assertThrows(
                        IncidentNotFoundException.class,
                        () -> incidentService.deleteIncident(incidentId)
                );

        assertTrue(exception.getMessage().contains("99"));

        verify(incidentRepository)
                .existsById(incidentId);

        verify(incidentRepository, never())
                .deleteById(anyLong());
    }

    @Test
    void shouldGetAllIncidentsSuccessfully() {

        // Given
        Incident incident = new Incident();

        incident.setId(1L);
        incident.setTitle("Database Down");
        incident.setSeverity(Severity.HIGH);
        incident.setStatus(IncidentStatus.OPEN);

        Page<Incident> incidentPage =
                new PageImpl<>(List.of(incident));

        when(
                incidentRepository.findAll(
                        any(Specification.class),
                        any(Pageable.class)
                )
        ).thenReturn(incidentPage);

        // When
        PageResponseDTO<IncidentResponseDTO> response =
                incidentService.getAllIncidents(
                        0,
                        10,
                        "detectedAt",
                        "desc",
                        null,
                        null,
                        null
                );

        // Then
        assertNotNull(response);

        assertEquals(
                1,
                response.getContent().size()
        );

        assertEquals(
                "Database Down",
                response.getContent()
                        .getFirst()
                        .getTitle()
        );

        verify(incidentRepository)
                .findAll(
                        any(Specification.class),
                        any(Pageable.class)
                );
    }
}
