package cz.sinko.moneymanager.facade;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import cz.sinko.moneymanager.config.exception.ResourceNotFoundException;
import cz.sinko.moneymanager.facade.dto.AccountDto;
import cz.sinko.moneymanager.facade.mapper.AccountMapper;
import cz.sinko.moneymanager.repository.model.Account;
import cz.sinko.moneymanager.service.AccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * Facade for account operations.
 *
 * @author Sinko Radovan
 */
@AllArgsConstructor
@Component
@Slf4j
public class AccountFacade {

    private final AccountService accountService;

    /**
     * Create account.
     *
     * @param accountDto account DTO
     * @return created account DTO
     */
    public AccountDto createAccount(final AccountDto accountDto) {
        return AccountMapper.t().mapAccount(accountService.createAccount(accountDto));
    }

    /**
     * Get all accounts.
     *
     * @return list of accounts
     */
    public List<AccountDto> getAccounts() {
        final List<Account> accounts = accountService.find(Sort.by("id").ascending());
        return AccountMapper.t().map(accounts);
    }

    /**
     * Update account.
     *
     * @param id account ID
     * @param accountDto account DTO
     * @return updated account DTO
     * @throws ResourceNotFoundException if account was not found
     */
    public AccountDto updateAccount(final Long id, final AccountDto accountDto) throws ResourceNotFoundException {
        return AccountMapper.t().mapAccount(accountService.updateAccount(id, accountDto));
    }

    /**
     * Delete account.
     *
     * @param id account ID
     * @throws ResourceNotFoundException if account was not found
     */
    public void deleteAccount(final Long id) throws ResourceNotFoundException {
        accountService.deleteAccount(id);
    }
}
