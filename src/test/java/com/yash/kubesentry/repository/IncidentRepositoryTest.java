package com.yash.kubesentry.repository;

import com.yash.kubesentry.model.entity.Incident;
import com.yash.kubesentry.model.enums.IncidentStatus;
import com.yash.kubesentry.model.enums.Severity;
import com.yash.kubesentry.specification.IncidentSpecification;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.domain.Specification;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.ANY
)
class IncidentRepositoryTest {

    @Autowired
    private IncidentRepository incidentRepository;

    @Test
    void shouldSaveIncidentSuccessfully() {

        Incident incident =
                Incident.builder()
                        .title("Database Down")
                        .description("Postgres crashed")
                        .namespace("production")
                        .podName("postgres-0")
                        .nodeName("worker-1")
                        .severity(Severity.HIGH)
                        .status(IncidentStatus.OPEN)
                        .build();

        Incident savedIncident =
                incidentRepository.save(incident);

        assertNotNull(savedIncident);

        assertNotNull(savedIncident.getId());

        assertEquals(
                "Database Down",
                savedIncident.getTitle()
        );

        assertEquals(
                Severity.HIGH,
                savedIncident.getSeverity()
        );

        assertEquals(
                IncidentStatus.OPEN,
                savedIncident.getStatus()
        );
    }

    @Test
    void shouldFindIncidentById() {

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

        Optional<Incident> foundIncident =
                incidentRepository.findById(
                        incident.getId()
                );

        assertTrue(
                foundIncident.isPresent()
        );

        assertEquals(
                incident.getId(),
                foundIncident.get().getId()
        );

        assertEquals(
                "Database Down",
                foundIncident.get().getTitle()
        );

        assertEquals(
                Severity.HIGH,
                foundIncident.get().getSeverity()
        );
    }

    @Test
    void shouldFindAllIncidents() {

        incidentRepository.save(

                Incident.builder()
                        .title("Incident One")
                        .description("Description One")
                        .namespace("production")
                        .podName("pod-1")
                        .nodeName("worker-1")
                        .severity(Severity.HIGH)
                        .status(IncidentStatus.OPEN)
                        .build()

        );

        incidentRepository.save(

                Incident.builder()
                        .title("Incident Two")
                        .description("Description Two")
                        .namespace("production")
                        .podName("pod-2")
                        .nodeName("worker-2")
                        .severity(Severity.MEDIUM)
                        .status(IncidentStatus.OPEN)
                        .build()

        );

        List<Incident> incidents =
                incidentRepository.findAll();

        assertEquals(
                2,
                incidents.size()
        );
    }

    @Test
    void shouldSoftDeleteIncident() {

        Incident incident =
                incidentRepository.save(

                        Incident.builder()
                                .title("Delete Test")
                                .description("Soft Delete")
                                .namespace("production")
                                .podName("pod-1")
                                .nodeName("worker-1")
                                .severity(Severity.HIGH)
                                .status(IncidentStatus.OPEN)
                                .build()

                );

        Long id = incident.getId();

        incidentRepository.delete(incident);

        List<Incident> incidents =
                incidentRepository.findAll();

        assertTrue(
                incidents.stream()
                        .noneMatch(i -> i.getId().equals(id))
        );
    }

    @Test
    void shouldFilterByStatus() {

        incidentRepository.save(
                Incident.builder()
                        .title("Open Incident")
                        .description("desc")
                        .namespace("prod")
                        .podName("pod1")
                        .nodeName("worker1")
                        .severity(Severity.HIGH)
                        .status(IncidentStatus.OPEN)
                        .build()
        );

        incidentRepository.save(
                Incident.builder()
                        .title("Resolved Incident")
                        .description("desc")
                        .namespace("prod")
                        .podName("pod2")
                        .nodeName("worker2")
                        .severity(Severity.HIGH)
                        .status(IncidentStatus.RESOLVED)
                        .build()
        );

        List<Incident> incidents =
                incidentRepository.findAll(
                        IncidentSpecification.hasStatus(
                                IncidentStatus.OPEN
                        )
                );

        assertEquals(
                1,
                incidents.size()
        );

        assertEquals(
                IncidentStatus.OPEN,
                incidents.getFirst().getStatus()
        );
    }

    @Test
    void shouldFilterBySeverity() {

        incidentRepository.save(
                Incident.builder()
                        .title("High")
                        .description("desc")
                        .namespace("prod")
                        .podName("pod1")
                        .nodeName("worker1")
                        .severity(Severity.HIGH)
                        .status(IncidentStatus.OPEN)
                        .build()
        );

        incidentRepository.save(
                Incident.builder()
                        .title("Low")
                        .description("desc")
                        .namespace("prod")
                        .podName("pod2")
                        .nodeName("worker2")
                        .severity(Severity.LOW)
                        .status(IncidentStatus.OPEN)
                        .build()
        );

        List<Incident> incidents =
                incidentRepository.findAll(
                        IncidentSpecification.hasSeverity(
                                Severity.HIGH
                        )
                );

        assertEquals(
                1,
                incidents.size()
        );

        assertEquals(
                Severity.HIGH,
                incidents.getFirst().getSeverity()
        );
    }

    @Test
    void shouldSearchIncident() {

        incidentRepository.save(
                Incident.builder()
                        .title("Database Down")
                        .description("Postgres crashed")
                        .namespace("production")
                        .podName("postgres")
                        .nodeName("worker1")
                        .severity(Severity.HIGH)
                        .status(IncidentStatus.OPEN)
                        .build()
        );

        incidentRepository.save(
                Incident.builder()
                        .title("CPU High")
                        .description("Usage high")
                        .namespace("production")
                        .podName("cpu")
                        .nodeName("worker2")
                        .severity(Severity.MEDIUM)
                        .status(IncidentStatus.OPEN)
                        .build()
        );

        List<Incident> incidents =
                incidentRepository.findAll(
                        IncidentSpecification.hasSearch(
                                "Database"
                        )
                );

        assertEquals(
                1,
                incidents.size()
        );

        assertEquals(
                "Database Down",
                incidents.getFirst().getTitle()
        );
    }

    @Test
    void shouldFilterByStatusSeverityAndSearch() {

        incidentRepository.save(
                Incident.builder()
                        .title("Database Down")
                        .description("Postgres crashed")
                        .namespace("production")
                        .podName("postgres")
                        .nodeName("worker1")
                        .severity(Severity.HIGH)
                        .status(IncidentStatus.OPEN)
                        .build()
        );

        incidentRepository.save(
                Incident.builder()
                        .title("CPU High")
                        .description("CPU issue")
                        .namespace("production")
                        .podName("cpu")
                        .nodeName("worker2")
                        .severity(Severity.LOW)
                        .status(IncidentStatus.OPEN)
                        .build()
        );

        List<Incident> incidents =
                incidentRepository.findAll(

                        Specification

                                .where(
                                        IncidentSpecification.hasStatus(
                                                IncidentStatus.OPEN
                                        )
                                )

                                .and(
                                        IncidentSpecification.hasSeverity(
                                                Severity.HIGH
                                        )
                                )

                                .and(
                                        IncidentSpecification.hasSearch(
                                                "Database"
                                        )
                                )

                );

        assertEquals(
                1,
                incidents.size()
        );

        assertEquals(
                "Database Down",
                incidents.getFirst().getTitle()
        );
    }

    @Test
    void shouldReturnPagedResults() {

        for (int i = 1; i <= 5; i++) {

            incidentRepository.save(

                    Incident.builder()
                            .title("Incident " + i)
                            .description("Description")
                            .namespace("production")
                            .podName("pod-" + i)
                            .nodeName("worker")
                            .severity(Severity.HIGH)
                            .status(IncidentStatus.OPEN)
                            .build()

            );
        }

        Pageable pageable =
                PageRequest.of(
                        0,
                        2,
                        Sort.by("title").ascending()
                );

        Page<Incident> page =
                incidentRepository.findAll(
                        pageable
                );

        assertEquals(
                2,
                page.getContent().size()
        );

        assertEquals(
                5,
                page.getTotalElements()
        );

        assertEquals(
                3,
                page.getTotalPages()
        );
    }


}