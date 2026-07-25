package com.yash.kubesentry.model.entity;

import com.yash.kubesentry.model.enums.Role;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import org.hibernate.annotations.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import jakarta.validation.constraints.Email;
@Entity // represents a db
@Table(name = "users")
@Getter  // reduces boilerplate
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)

public class User{
    @Id // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // db should genereate
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; // not long st primitive long can't be ull
    @NotBlank
    @Column(nullable = false, unique = true)
    @Size(min = 3, max = 50)
    private String username;
    @NotBlank
    @Column(nullable = false, unique = true)
    @Email
    private String email;
    @NotBlank
    @Column(nullable = false)
    private String password;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private boolean enabled = true;
}
