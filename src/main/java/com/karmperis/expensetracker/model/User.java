package com.karmperis.expensetracker.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity: Represents a user table in the database.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends AbstractEntity{
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Builder.Default
    private Boolean active = true;
}