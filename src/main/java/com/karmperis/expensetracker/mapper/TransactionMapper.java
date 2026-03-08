package com.karmperis.expensetracker.mapper;

import com.karmperis.expensetracker.dto.TransactionEditDTO;
import com.karmperis.expensetracker.dto.TransactionInsertDTO;
import com.karmperis.expensetracker.dto.TransactionReadOnlyDTO;
import com.karmperis.expensetracker.model.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    /**
     * Maps TransactionInsertDTO to a transaction entity.
     * @param dto Containing the transactions details.
     * @return A new transaction entity populated with the DTO data.
     */
    public Transaction mapToEntity(TransactionInsertDTO dto) {
        Transaction transaction = new Transaction();
        transaction.setDate(dto.date());
        transaction.setAmount(dto.amount());
        transaction.setDescription(dto.description());
        transaction.setActive(true);
        return transaction;
    }

    /**
     * Maps a transaction entity to a TransactionReadOnlyDTO.
     * @param transaction The transaction entity to be mapped.
     * @return A TransactionReadOnlyDTO.
     */
    public TransactionReadOnlyDTO mapToReadOnlyDTO(Transaction transaction) {
        return new TransactionReadOnlyDTO(
                transaction.getUuid(),
                transaction.getDate(),
                transaction.getCategory().getId(),
                transaction.getCategory().getCategory(),
                transaction.getAmount(),
                transaction.getDescription(),
                transaction.getPaymentMethod().getId(),
                transaction.getPaymentMethod().getPaymentMethod(),
                transaction.getAccount().getId(),
                transaction.getAccount().getAccount(),
                transaction.getActive()
        );
    }

    /**
     * Updates an existing transaction entity using data from a TransactionEditDTO.
     * @param transaction The existing transaction entity to be updated.
     * @param dto TransactionEditDTO.
     */
    public void updateEntityFromEditDTO(Transaction transaction, TransactionEditDTO dto) {
        transaction.setDate(dto.date());
        transaction.setAmount(dto.amount());
        transaction.setDescription(dto.description());
        if (dto.active() != null) {
            transaction.setActive(dto.active());
        }
    }
}