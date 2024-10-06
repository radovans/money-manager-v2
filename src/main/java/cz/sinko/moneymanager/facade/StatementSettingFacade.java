package cz.sinko.moneymanager.facade;

import org.springframework.stereotype.Component;

import cz.sinko.moneymanager.config.exception.ResourceNotFoundException;
import cz.sinko.moneymanager.facade.dto.StatementSettingDto;
import cz.sinko.moneymanager.facade.mapper.StatementSettingMapper;
import cz.sinko.moneymanager.repository.model.Account;
import cz.sinko.moneymanager.repository.model.StatementSetting;
import cz.sinko.moneymanager.service.AccountService;
import cz.sinko.moneymanager.service.StatementSettingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * Facade for statement setting operations.
 *
 * @author Sinko Radovan
 */
@AllArgsConstructor
@Component
@Slf4j
public class StatementSettingFacade {

    private final AccountService accountService;

    private final StatementSettingService statementSettingService;

    /**
     * Create statement setting.
     *
     * @param statementSettingDto statement setting DTO
     * @return created statement setting DTO
     * @throws ResourceNotFoundException if account was not found
     */
    public StatementSettingDto createStatementSetting(final StatementSettingDto statementSettingDto) throws ResourceNotFoundException {
        final StatementSetting statementSetting = StatementSettingMapper.t().map(statementSettingDto);
        final Account account = accountService.find(statementSettingDto.getAccount());
        return StatementSettingMapper.t().map(statementSettingService.createStatementSetting(statementSetting, account));
    }
}
