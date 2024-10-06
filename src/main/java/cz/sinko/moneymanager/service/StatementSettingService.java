package cz.sinko.moneymanager.service;

import org.springframework.stereotype.Component;

import cz.sinko.moneymanager.repository.StatementSettingRepository;
import cz.sinko.moneymanager.repository.model.Account;
import cz.sinko.moneymanager.repository.model.StatementSetting;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * Service for subcategory operations.
 *
 * @author Sinko Radovan
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class StatementSettingService {

    private final StatementSettingRepository statementSettingRepository;

    /**
     * Create statement setting.
     *
     * @param statementSetting statement setting
     * @param account account
     * @return created statement setting
     */
    public StatementSetting createStatementSetting(final StatementSetting statementSetting, final Account account) {
        statementSetting.setAccount(account);
        return statementSettingRepository.save(statementSetting);
    }
}
