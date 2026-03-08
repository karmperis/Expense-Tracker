package com.karmperis.expensetracker.mapper;

import com.karmperis.expensetracker.dto.PaymentMethodEditDTO;
import com.karmperis.expensetracker.dto.PaymentMethodInsertDTO;
import com.karmperis.expensetracker.dto.PaymentMethodReadOnlyDTO;
import com.karmperis.expensetracker.model.PaymentMethod;
import org.springframework.stereotype.Component;

@Component
public class PaymentMethodMapper {

    /**
     * Maps PaymentMethodInsertDTO to a payment method type entity.
     * @param dto Containing the payment method details.
     * @return A new payment method entity populated with the DTO data.
     */
    public PaymentMethod mapToEntity(PaymentMethodInsertDTO dto) {
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setPaymentMethod(dto.paymentMethod());
        paymentMethod.setActive(true);
        return paymentMethod;
    }

    /**
     * Maps a payment method entity to a PaymentMethodReadOnlyDTO.
     * @param paymentMethod The payment method entity to be mapped.
     * @return A PaymentMethodReadOnlyDTO.
     */
    public PaymentMethodReadOnlyDTO mapToReadOnlyDTO(PaymentMethod paymentMethod) {
        return new PaymentMethodReadOnlyDTO(
                paymentMethod.getUuid(),
                paymentMethod.getPaymentMethod(),
                paymentMethod.getActive()
        );
    }

    /**
     * Updates an existing payment method entity using data from a PaymentMethodEditDTO.
     * @param paymentMethod The existing paymentMethod entity to be updated.
     * @param dto PaymentMethodEditDTO.
     */
    public void updateEntityFromEditDTO(PaymentMethod paymentMethod, PaymentMethodEditDTO dto) {
        paymentMethod.setPaymentMethod(dto.paymentMethod());
        if (dto.active() != null) {
            paymentMethod.setActive(dto.active());
        }
    }
}