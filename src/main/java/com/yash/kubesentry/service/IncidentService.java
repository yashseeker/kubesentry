package com.yash.kubesentry.service;
import com.yash.kubesentry.dto.IncidentRequestDTO;
import com.yash.kubesentry.dto.IncidentResponseDTO;
import com.yash.kubesentry.dto.PageResponseDTO;
import com.yash.kubesentry.model.enums.IncidentStatus;
//import com.yash.kubesentry.model.entity.Incident;
import com.yash.kubesentry.model.enums.Severity;
import org.springframework.data.domain.Page;
import java.util.List;
import org.springframework.data.domain.Page;
import com.yash.kubesentry.dto.PageResponseDTO;

public interface IncidentService {
    IncidentResponseDTO saveIncident(IncidentRequestDTO requestDTO);
    PageResponseDTO<IncidentResponseDTO> getAllIncidents(
            int page,
            int size,
            String sortBy,
            String sortDir,
            IncidentStatus status,
            Severity severity,
            String search
    );
    IncidentResponseDTO getIncidentById(Long id);
    void deleteIncident(Long id);
    IncidentResponseDTO updateIncidentStatus(
            Long id,
            IncidentStatus status);
}
