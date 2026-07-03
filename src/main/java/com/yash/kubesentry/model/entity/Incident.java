package com.yash.kubesentry.model.entity;
import jakarta.persistence.*;
import com.yash.kubesentry.model.enums.IncidentStatus;
import com.yash.kubesentry.model.enums.Severity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity // represents a db
@Table(name = "incidents")
@Getter  // reduces boilerplate
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor

public class Incident{
 @Id // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // db should genereate
    private Long id; // not long st primitive long can't be ull
    @NotBlank
    @Size(max = 200)
    private String title;
    @NotBlank
    private String description;
    private String namespace;
    private String podName;
    private String nodeName;

   @Enumerated(EnumType.STRING) //
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
