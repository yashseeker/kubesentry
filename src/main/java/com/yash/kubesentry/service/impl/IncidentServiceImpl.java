package com.yash.kubesentry.service.impl;

import com.yash.kubesentry.dto.IncidentRequestDTO;
import com.yash.kubesentry.dto.IncidentResponseDTO;
import com.yash.kubesentry.exception.IncidentNotFoundException;
import com.yash.kubesentry.mapper.IncidentMapper;
import com.yash.kubesentry.model.entity.Incident;
import com.yash.kubesentry.repository.IncidentRepository;
import com.yash.kubesentry.service.IncidentService;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


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
        logger.info("Creating new incident with title: {}", requestDTO.getTitle());
        Incident incident = IncidentMapper.toEntity(requestDTO);

        Incident savedIncident = incidentRepository.save(incident);

        logger.info("Incident created successfully with id: {}", savedIncident.getId());

        return IncidentMapper.toResponseDTO(savedIncident);
    }

    @Override
    public List<IncidentResponseDTO> getAllIncidents() {
        return incidentRepository.findAll()
                .stream()
                .map(IncidentMapper::toResponseDTO)
                .collect(Collectors.toList());
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
        incidentRepository.deleteById(id);
    }
}
