package com.yash.kubesentry.model.entity;
import jakarta.persistence.*;
import com.yash.kubesentry.model.enums.IncidentStatus;
import com.yash.kubesentry.model.enums.Severity;

import lombok.*;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "incidents")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor

public class Incident{
 @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String namespace;
    private String podName;
    private String nodeName;

   @Enumerated(EnumType.STRING)
   private Severity severity;

   @Enumerated(EnumType.STRING)
   private IncidentStatus status;

//   private LocalDateTime detectedAt;
//
//   private LocalDateTime updatedAt;

   @CreationTimestamp
   private LocalDateTime detectedAt;

   @UpdateTimestamp
   private LocalDateTime updatedAt;
}
