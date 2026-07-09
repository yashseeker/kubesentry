package com.yash.kubesentry.service.impl;
import com.yash.kubesentry.dto.PageResponseDTO;
import com.yash.kubesentry.model.enums.IncidentStatus;
import com.yash.kubesentry.dto.IncidentRequestDTO;
import com.yash.kubesentry.dto.IncidentResponseDTO;
import com.yash.kubesentry.exception.IncidentNotFoundException;
import com.yash.kubesentry.mapper.IncidentMapper;
import com.yash.kubesentry.model.entity.Incident;
import com.yash.kubesentry.model.enums.Severity;
import com.yash.kubesentry.repository.IncidentRepository;
import com.yash.kubesentry.service.IncidentService;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.yash.kubesentry.util.StatusTransitionValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import com.yash.kubesentry.specification.IncidentSpecification;
@Service
public class IncidentServiceImpl implements IncidentService {

    private final IncidentRepository incidentRepository;

    public IncidentServiceImpl(IncidentRepository incidentRepository) {
        this.incidentRepository = incidentRepository;
    }
    private static final Logger logger =
            LoggerFactory.getLogger(IncidentServiceImpl.class);
    @Override
    public IncidentResponseDTO saveIncident(IncidentRequestDTO requestDTO) {
        logger.info("Running 2 , Creating new incident with title: {}", requestDTO.getTitle());
        Incident incident = IncidentMapper.toEntity(requestDTO);
        incident.setStatus(IncidentStatus.OPEN);
        Incident savedIncident = incidentRepository.save(incident);

        logger.info("Incident created successfully with id: {}", savedIncident.getId());

        return IncidentMapper.toResponseDTO(savedIncident);
    }

    @Override
    public PageResponseDTO<IncidentResponseDTO> getAllIncidents(
        int page,
        int size,
        String sortBy,
        String sortDir,
        IncidentStatus status,
        Severity severity,
        String search
    ) {

            Sort sort = sortDir.equalsIgnoreCase("asc")
                    ? Sort.by(sortBy).ascending()
                    : Sort.by(sortBy).descending();

            Pageable pageable = PageRequest.of(page, size, sort);

        Specification<Incident> specification =
                Specification
                        .where(IncidentSpecification.hasStatus(status))
                        .and(IncidentSpecification.hasSeverity(severity))
                        .and(IncidentSpecification.hasSearch(search));
        Page<Incident> incidents =
                incidentRepository.findAll(
                        specification,
                        pageable
                );

        PageResponseDTO<IncidentResponseDTO> response =
                PageResponseDTO.<IncidentResponseDTO>builder()
                        .content(
                                incidents.getContent()
                                        .stream()
                                        .map(IncidentMapper::toResponseDTO)
                                        .toList()
                        )
                        .page(incidents.getNumber())
                        .size(incidents.getSize())
                        .totalElements(incidents.getTotalElements())
                        .totalPages(incidents.getTotalPages())
                        .first(incidents.isFirst())
                        .last(incidents.isLast())
                        .empty(incidents.isEmpty())
                        .build();

        return response;
    }

    @Override
    public IncidentResponseDTO  getIncidentById(Long id) {
        logger.info("Fetching incident with id: {}", id);
        Incident incident = incidentRepository.findById(id)
                .orElseThrow(() -> new IncidentNotFoundException(id));

        return IncidentMapper.toResponseDTO(incident);
    }

    @Override
    public void deleteIncident(Long id) {
        logger.info("Deleting incident with id: {}", id);
        if (!incidentRepository.existsById(id)) {
            throw new IncidentNotFoundException(id);
        }
        incidentRepository.deleteById(id);
    }

    @Override
    public IncidentResponseDTO updateIncidentStatus(Long id, IncidentStatus status) {

        Incident incident = incidentRepository.findById(id)
                .orElseThrow(() -> new IncidentNotFoundException(id));

        StatusTransitionValidator.validate(
                incident.getStatus(),
                status
        );

        incident.setStatus(status);

        Incident updatedIncident = incidentRepository.save(incident);

        return IncidentMapper.toResponseDTO(updatedIncident);
    }
}
