package com.karmperis.expensetracker.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entity: Represents a financial transaction (income or expense) in the database.
 */
@Entity
@Table(name = "transactions")
@Check(constraints = "amount > 0")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_method_id", nullable = false)
    private PaymentMethod paymentMethod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder.Default
    private Boolean active = true;
}