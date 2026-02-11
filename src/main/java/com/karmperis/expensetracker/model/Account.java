package com.karmperis.expensetracker.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

/**
 * Entity: Represents an account table in the database.
 */

@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account extends AbstractEntity {

    @Column(nullable = false)
    private String account;

    @Builder.Default
    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder.Default
    private Boolean active = true;
}