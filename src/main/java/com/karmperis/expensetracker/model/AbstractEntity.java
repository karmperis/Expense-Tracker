package com.karmperis.expensetracker.model;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.Instant;
import java.util.UUID;

/**
 * This class provides auditing functionality by automatically updating
 * the created_at and updated_at timestamps when an entity
 * is created or updated. Also provides UUID for security reasons.
 */
@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractEntity {
    @CreatedDate
    @Column(name ="created_at", nullable = false, updatable = false, columnDefinition = "DATETIME")
    private Instant created_at;

    @LastModifiedDate
    @Column(name ="updated_at", nullable = false, columnDefinition = "DATETIME")
    private Instant updated_at;

    @Column(unique = true, nullable = false, updatable = false)
    private UUID uuid = UUID.randomUUID();
}