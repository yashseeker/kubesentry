package com.yash.kubesentry.service;
import com.yash.kubesentry.dto.IncidentRequestDTO;
import com.yash.kubesentry.dto.IncidentResponseDTO;
//import com.yash.kubesentry.model.entity.Incident;

import java.util.List;

public interface IncidentService {
    IncidentResponseDTO saveIncident(IncidentRequestDTO requestDTO);
    List<IncidentResponseDTO> getAllIncidents();
    IncidentResponseDTO getIncidentById(Long id);
    void deleteIncident(Long id);
}
