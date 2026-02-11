package com.karmperis.expensetracker.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity: Represents a category type table in the database.
 */
@Entity
@Table(name = "categories_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryType extends AbstractEntity {
    @Column(nullable = false, unique = true)
    private String type;

    @Builder.Default
    private Boolean active = true;
}