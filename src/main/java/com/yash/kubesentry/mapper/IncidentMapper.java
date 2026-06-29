package com.yash.kubesentry.mapper;

import com.yash.kubesentry.dto.IncidentRequestDTO;
import com.yash.kubesentry.dto.IncidentResponseDTO;
import com.yash.kubesentry.model.entity.Incident;
import com.yash.kubesentry.model.enums.IncidentStatus;

        public class IncidentMapper {
            public static Incident toEntity(IncidentRequestDTO dto) {
                return Incident.builder()
                        .title(dto.getTitle())
                        .description(dto.getDescription())
                        .namespace(dto.getNamespace())
                        .podName(dto.getPodName())
                        .nodeName(dto.getNodeName())
                        .severity(dto.getSeverity())
                        .status(IncidentStatus.OPEN)
                        .build(); //
            }
            public static IncidentResponseDTO toResponseDTO(Incident incident) {return IncidentResponseDTO.builder()
                        .id(incident.getId())
                        .title(incident.getTitle())
                        .description(incident.getDescription())
                        .namespace(incident.getNamespace())
                        .podName(incident.getPodName())
                        .nodeName(incident.getNodeName())
                        .severity(incident.getSeverity())
                        .status(incident.getStatus())
                        .detectedAt(incident.getDetectedAt())
                        .updatedAt(incident.getUpdatedAt())
                        .build(); // ?
            }
        }

