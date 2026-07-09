package com.yash.kubesentry.model.entity;
import jakarta.persistence.*;
import com.yash.kubesentry.model.enums.IncidentStatus;
import com.yash.kubesentry.model.enums.Severity;
import jakarta.persistence.Table;
import org.hibernate.annotations.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
@Entity // represents a db
@Table(name = "incidents")
@SQLDelete(sql = "UPDATE incidents SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
@Getter  // reduces boilerplate
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
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

   @Setter
   @Enumerated(EnumType.STRING)
   private IncidentStatus status;

//   private LocalDateTime detectedAt;
//
//   private LocalDateTime updatedAt;


    @CreatedDate
    private LocalDateTime detectedAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private boolean deleted = false;

//    @SQLDelete(sql = "UPDATE incidents SET deleted = true WHERE id=?")
//    @Where(clause = "deleted = false")

}
