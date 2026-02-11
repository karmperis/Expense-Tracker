package com.karmperis.expensetracker.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity: Represents a payment_method table in the database.
 */

@Entity
@Table(name = "payment_methods")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentMethod extends AbstractEntity {
    @Column(nullable = false)
    private String paymentMethod;

    @Builder.Default
    private Boolean active = true;
}