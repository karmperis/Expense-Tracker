package com.karmperis.expensetracker.mapper;

import com.karmperis.expensetracker.dto.*;
import com.karmperis.expensetracker.model.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    /**
     * Maps AccountInsertDTO to an account entity.
     * @param dto Containing the account details.
     * @return A new account entity populated with the DTO data.
     */
    public Account mapToEntity(AccountInsertDTO dto) {
        Account account = new Account();
        account.setAccount(dto.account());
        account.setActive(true);
        return account;
    }

    /**
     * Maps an account entity to an AccountReadOnlyDTO.
     * @param account The account entity to be mapped.
     * @return An AccountReadOnlyDTO
     */
    public AccountReadOnlyDTO mapToReadOnlyDTO(Account account) {
        return new AccountReadOnlyDTO(
                account.getUuid(),
                account.getAccount(),
                account.getBalance(),
                account.getActive()
        );
    }

    /**
     * Updates an existing account entity using data from an AccountEditDTO.
     * @param account The existing account entity to be updated.
     * @param dto AccountEditDTO
     */
    public void updateEntityFromEditDTO(Account account, AccountEditDTO dto) {
        account.setAccount(dto.account());
        if (dto.active() != null) {
            account.setActive(dto.active());
        }
    }
}