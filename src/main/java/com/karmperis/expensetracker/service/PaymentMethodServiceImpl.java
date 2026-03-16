package com.karmperis.expensetracker.service;

import com.karmperis.expensetracker.core.exceptions.EntityAlreadyExistsException;
import com.karmperis.expensetracker.core.exceptions.EntityNotFoundException;
import com.karmperis.expensetracker.dto.PaymentMethodEditDTO;
import com.karmperis.expensetracker.dto.PaymentMethodInsertDTO;
import com.karmperis.expensetracker.dto.PaymentMethodReadOnlyDTO;
import com.karmperis.expensetracker.mapper.PaymentMethodMapper;
import com.karmperis.expensetracker.model.PaymentMethod;
import com.karmperis.expensetracker.model.User;
import com.karmperis.expensetracker.repository.PaymentMethodRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Implementation of the IPaymentMethodService for managing payment methods.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentMethodServiceImpl implements IPaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentMethodMapper mapper;

    @Override
    @Transactional(rollbackFor = EntityAlreadyExistsException.class)
    public PaymentMethodReadOnlyDTO savePaymentMethod(PaymentMethodInsertDTO dto, User user)
            throws EntityAlreadyExistsException {

        try {
            if (paymentMethodRepository.existsByUserAndPaymentMethod(user, dto.paymentMethod())) {
                throw new EntityAlreadyExistsException("Payment method", "Payment method '" + dto.paymentMethod() + "' already exists.");
            }

            PaymentMethod paymentMethod = mapper.mapToEntity(dto);
            paymentMethod.setUser(user);
            paymentMethod.setActive(true);

            paymentMethodRepository.save(paymentMethod);
            log.info("Payment method '{}' saved successfully for user '{}'.", dto.paymentMethod(), user.getUsername());

            return mapper.mapToReadOnlyDTO(paymentMethod);

        } catch (EntityAlreadyExistsException e) {
            log.error("Save failed: Payment method already exists for user={}", user.getUsername());
            throw e;
        }
    }

    @Override
    @Transactional(rollbackFor = {EntityNotFoundException.class, EntityAlreadyExistsException.class})
    public PaymentMethodReadOnlyDTO updatePaymentMethod(UUID uuid, PaymentMethodEditDTO dto, User user)
            throws EntityNotFoundException, EntityAlreadyExistsException {

        try {
            PaymentMethod existingMethod = paymentMethodRepository.findByUuidAndUser(uuid, user)
                    .orElseThrow(() -> new EntityNotFoundException("Payment method", "Payment method not found with uuid=" + uuid));

            if (!existingMethod.getPaymentMethod().equals(dto.paymentMethod()) &&
                    paymentMethodRepository.existsByUserAndPaymentMethod(user, dto.paymentMethod())) {
                throw new EntityAlreadyExistsException("Payment method", "Payment method '" + dto.paymentMethod() + "' already exists.");
            }

            mapper.updateEntityFromEditDTO(existingMethod, dto);

            paymentMethodRepository.save(existingMethod);
            log.info("Payment method with uuid={} updated successfully.", uuid);

            return mapper.mapToReadOnlyDTO(existingMethod);

        } catch (EntityNotFoundException | EntityAlreadyExistsException e) {
            log.error("Update failed for payment method with uuid={}", uuid, e);
            throw e;
        }
    }

    @Override
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public void deletePaymentMethod(UUID uuid, User user) throws EntityNotFoundException {
        try {
            PaymentMethod paymentMethod = paymentMethodRepository.findByUuidAndUser(uuid, user)
                    .orElseThrow(() -> new EntityNotFoundException("Payment method", "Payment method not found with uuid=" + uuid));

            paymentMethod.setActive(false); // Soft Delete
            log.info("Payment method with uuid={} deactivated successfully.", uuid);
        } catch (EntityNotFoundException e) {
            log.error("Delete failed for payment method with uuid={}", uuid, e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentMethodReadOnlyDTO getPaymentMethodByUuid(UUID uuid, User user) throws EntityNotFoundException {
        return paymentMethodRepository.findByUuidAndUser(uuid, user)
                .map(mapper::mapToReadOnlyDTO)
                .orElseThrow(() -> new EntityNotFoundException("Payment method", "Payment method not found with uuid=" + uuid));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentMethodReadOnlyDTO> getAllPaymentMethods(Pageable pageable, User user) {
        return paymentMethodRepository.findByUserAndActiveTrueOrderByPaymentMethodAsc(user, pageable)
                .map(mapper::mapToReadOnlyDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isPaymentMethodExists(String paymentMethod, User user) {
        return paymentMethodRepository.existsByUserAndPaymentMethod(user, paymentMethod);
    }
}