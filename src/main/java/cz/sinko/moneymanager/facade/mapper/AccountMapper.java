package cz.sinko.moneymanager.facade.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import cz.sinko.moneymanager.facade.dto.AccountDto;
import cz.sinko.moneymanager.repository.model.Account;


/**
 * Mapper for Account entity.
 *
 * @author Sinko Radovan
 */
@Mapper()
public interface AccountMapper {

    /**
     * Get instance of AccountMapper.
     *
     * @return instance of AccountMapper
     */
    static AccountMapper t() {
        return Mappers.getMapper(AccountMapper.class);
    }

    /**
     * Map Account to AccountDto
     *
     * @param source account
     * @return accountDto
     */
    AccountDto mapAccount(final Account source);

    /**
     * Map AccountDto to Account
     *
     * @param source accountDto
     * @return account
     */
    Account map(final AccountDto source);

    /**
     * Map list of accounts to list of accountDto.
     *
     * @param source list of accounts
     * @return list of accountDto
     */
    List<AccountDto> map(final List<Account> source);
}
