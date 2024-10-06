package cz.sinko.moneymanager.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import cz.sinko.moneymanager.config.exception.ResourceNotFoundException;
import cz.sinko.moneymanager.facade.dto.AccountDto;
import cz.sinko.moneymanager.facade.mapper.AccountMapper;
import cz.sinko.moneymanager.repository.AccountRepository;
import cz.sinko.moneymanager.repository.model.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * Service for account operations.
 *
 * @author Sinko Radovan
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;

    /**
     * Create account.
     *
     * @param accountDto account DTO
     * @return created account
     */
    public Account createAccount(final AccountDto accountDto) {
        final Account account = AccountMapper.t().map(accountDto);
        return accountRepository.save(account);
    }

    /**
     * Find account by ID.
     *
     * @param accountId account ID
     * @return account
     * @throws ResourceNotFoundException if account was not found
     */
    public Account find(final Long accountId) throws ResourceNotFoundException {
        return accountRepository.findById(accountId)
            .orElseThrow(() -> ResourceNotFoundException.createWith("Account", " with id '" + accountId + "' was not found"));
    }

    /**
     * Find all accounts.
     *
     * @param sort sort
     * @return list of accounts
     */
    public List<Account> find(final Sort sort) {
        return accountRepository.findAll(sort);
    }

    /**
     * Update account.
     *
     * @param id account ID
     * @param accountDto account DTO
     * @return updated account
     * @throws ResourceNotFoundException if account was not found
     */
    public Account updateAccount(final Long id, final AccountDto accountDto) throws ResourceNotFoundException {
        final Account account = find(id);
        account.setName(accountDto.getName());
        return accountRepository.save(account);
    }

    /**
     * Delete account.
     *
     * @param id account ID
     * @throws ResourceNotFoundException if account was not found
     */
    public void deleteAccount(final Long id) throws ResourceNotFoundException {
        if (!accountRepository.existsById(id)) {
            throw ResourceNotFoundException.createWith("Account", " with id '" + id + "' was not found");
        }
        accountRepository.deleteById(id);
    }
}

