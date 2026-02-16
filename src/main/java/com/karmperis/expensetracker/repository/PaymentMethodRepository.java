package com.karmperis.expensetracker.repository;

import com.karmperis.expensetracker.model.PaymentMethod;
import com.karmperis.expensetracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Interface for managing payment method entities.
 */
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
    /**
     * Retrieves a payment method based on UUID and user.
     * @param uuid The UUID of the payment method to search for.
     * @param user The user who owns the payment method.
     * @return The found payment method, or empty if no payment method matches the UUID.
     */
    Optional<PaymentMethod> findByUuidAndUser(UUID uuid, User user);

    /**
     * Retrieves all active payment methods associated with a specific user.
     * @param user The user entity to filter payment methods by.
     * @return A list of active payment methods belonging to the specified user.
     */
    List<PaymentMethod> findByUserAndActiveTrueOrderByPaymentMethodAsc(User user);

    /**
     * Retrieves all inactive payment methods associated with a specific user.
     * @param user The User entity to filter payment methods by.
     * @return A list of inactive payment methods belonging to the specified user.
     */
    List<PaymentMethod> findByUserAndActiveFalseOrderByPaymentMethodAsc(User user);

    /**
     * Checks if a payment method with the given name already exists for a specific user.
     * @param user The user entity to check.
     * @param paymentMethod The name of the payment method to check.
     * @return True if a matching payment method exists, false otherwise.
     */
    boolean existsByUserAndPaymentMethod(User user, String paymentMethod);
}