package cz.sinko.moneymanager.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import jakarta.annotation.PostConstruct;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import cz.sinko.moneymanager.config.exception.ResourceNotFoundException;
import cz.sinko.moneymanager.facade.AccountFacade;
import cz.sinko.moneymanager.facade.CategoryFacade;
import cz.sinko.moneymanager.facade.RuleFacade;
import cz.sinko.moneymanager.facade.StatementSettingFacade;
import cz.sinko.moneymanager.facade.SubcategoryFacade;
import cz.sinko.moneymanager.facade.dto.AccountDto;
import cz.sinko.moneymanager.facade.dto.CategoryDto;
import cz.sinko.moneymanager.facade.dto.Configurator;
import cz.sinko.moneymanager.facade.dto.RuleDto;
import cz.sinko.moneymanager.facade.dto.StatementSettingDto;
import cz.sinko.moneymanager.facade.dto.SubcategoryDto;
import cz.sinko.moneymanager.repository.model.RuleType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * Autoconfiguration for importing configurator.
 *
 * @author Sinko Radovan
 */
@Component
@ConditionalOnProperty(value = ConfigurationConstants.ENABLED, prefix = ConfigurationConstants.CONFIGURATOR_PREFIX)
@EnableConfigurationProperties(ConfiguratorProperties.class)
@RequiredArgsConstructor
@Slf4j
public class ConfiguratorAutoConfiguration {

    private final ConfiguratorProperties configuratorProperties;

    private final AccountFacade accountFacade;

    private final CategoryFacade categoryFacade;

    private final RuleFacade ruleFacade;

    private final StatementSettingFacade statementSettingFacade;

    private final SubcategoryFacade subcategoryFacade;

    @PostConstruct
    public void init() {
        log.info("Initializing ConfiguratorAutoConfiguration.");

        Assert.notNull(configuratorProperties.getFilePath(), ConfigurationConstants.CONFIGURATOR_PREFIX + ".file-path must be set.");

        initConfiguration();
        log.info("ConfiguratorAutoConfiguration initialized.");
    }

    private void initConfiguration() {
        final Configurator configurator = loadConfiguration();
        setupAccounts(configurator.getAccounts());
        setupCategories(configurator.getCategories());
        setupSubcategories(configurator.getSubcategories());
        setupRules(configurator.getRules());
        setupStatementSettings(configurator.getStatementSettings());
    }

    private Configurator loadConfiguration() {
        final File file = new File(configuratorProperties.getFilePath());

        final Configurator configurator = new Configurator();

        try (FileInputStream fis = new FileInputStream(file);
            final Workbook workbook = new XSSFWorkbook(fis)) {

            configurator.setAccounts(getAccounts(workbook));
            configurator.setCategories(getCategories(workbook));
            configurator.setSubcategories(getSubcategories(workbook));
            configurator.setRules(getRules(workbook));
            configurator.setStatementSettings(getStatementSettings(workbook));

        } catch (IOException e) {
            log.error("Error reading configuration file", e);
        }

        return configurator;
    }

    private Set<String> getAccounts(final Workbook workbook) {
        Assert.notNull(configuratorProperties.getAccount().getSheetName(), "configurator.account.sheet-name must be set.");
        Assert.notNull(configuratorProperties.getAccount().getNameColumn(), "configurator.account.name-column must be set.");

        final Sheet accountsSheet = workbook.getSheet(configuratorProperties.getAccount().getSheetName());
        final Set<String> accounts = new HashSet<>();
        for (Row row : accountsSheet) {
            if (row.getRowNum() == 0) {
                continue; // Skip header
            }
            if (!row.getCell(configuratorProperties.getAccount().getNameColumn()).getStringCellValue().isBlank()) {
                accounts.add(row.getCell(configuratorProperties.getAccount().getNameColumn()).getStringCellValue());
            }
        }
        return accounts;
    }

    private Set<String> getCategories(final Workbook workbook) {
        Assert.notNull(configuratorProperties.getCategory().getSheetName(), "configurator.category.sheet-name must be set.");
        Assert.notNull(configuratorProperties.getCategory().getNameColumn(), "configurator.category.name-column must be set.");

        final Sheet categoriesSheet = workbook.getSheet(configuratorProperties.getCategory().getSheetName());
        final Set<String> categories = new HashSet<>();
        for (Row row : categoriesSheet) {
            if (row.getRowNum() == 0) {
                continue; // Skip header
            }
            if (!row.getCell(configuratorProperties.getCategory().getNameColumn()).getStringCellValue().isBlank()) {
                categories.add(row.getCell(configuratorProperties.getCategory().getNameColumn()).getStringCellValue());
            }
        }
        return categories;
    }

    private Map<String, String> getSubcategories(final Workbook workbook) {
        Assert.notNull(configuratorProperties.getSubcategory().getSheetName(), "configurator.subcategory.sheet-name must be set.");
        Assert.notNull(configuratorProperties.getSubcategory().getNameColumn(), "configurator.subcategory.name-column must be set.");
        Assert.notNull(configuratorProperties.getSubcategory().getCategoryColumn(), "configurator.subcategory.category-column must be set.");

        final Sheet categoriesSheet = workbook.getSheet(configuratorProperties.getSubcategory().getSheetName());
        final Map<String, String> subcategories = new HashMap<>();
        for (Row row : categoriesSheet) {
            if (row.getRowNum() == 0) {
                continue; // Skip header
            }
            if (!row.getCell(configuratorProperties.getSubcategory().getNameColumn()).getStringCellValue().isBlank()) {
                subcategories.put(
                    row.getCell(configuratorProperties.getSubcategory().getNameColumn()).getStringCellValue(),
                    row.getCell(configuratorProperties.getSubcategory()
                        .getCategoryColumn()).getStringCellValue());
            }
        }
        return subcategories;
    }

    private Set<RuleDto> getRules(final Workbook workbook) {
        Assert.notNull(configuratorProperties.getRule().getSheetName(), "configurator.rule.sheet-name must be set.");
        Assert.notNull(configuratorProperties.getRule().getKeyColumn(), "configurator.rule.key-column must be set.");
        Assert.notNull(configuratorProperties.getRule().getTypeColumn(), "configurator.rule.type-column must be set.");
        Assert.notNull(configuratorProperties.getRule().getRecipientColumn(), "configurator.rule.recipient-column must be set.");
        Assert.notNull(configuratorProperties.getRule().getNoteColumn(), "configurator.rule.note-column must be set.");
        Assert.notNull(configuratorProperties.getRule().getSubcategoryColumn(), "configurator.rule.subcategory-column must be set.");
        Assert.notNull(configuratorProperties.getRule().getLabelColumn(), "configurator.rule.label-column must be set.");
        Assert.notNull(configuratorProperties.getRule().getSkipTransactionColumn(), "configurator.rule.skip-transaction-column must be set.");

        final Sheet rulesSheet = workbook.getSheet(configuratorProperties.getRule().getSheetName());
        final Set<RuleDto> rules = new HashSet<>();
        for (Row row : rulesSheet) {
            if (row.getRowNum() == 0) {
                continue; // Skip header
            }
            if (!row.getCell(configuratorProperties.getRule().getKeyColumn()).getStringCellValue().isBlank()) {
                final RuleDto ruleDto = RuleDto.builder().key(row.getCell(configuratorProperties.getRule().getKeyColumn()).getStringCellValue())
                    .type(RuleType.valueOf(row.getCell(configuratorProperties.getRule().getTypeColumn()).getStringCellValue()))
                    .recipient(row.getCell(configuratorProperties.getRule().getRecipientColumn()).getStringCellValue())
                    .note(row.getCell(configuratorProperties.getRule().getNoteColumn()).getStringCellValue())
                    .category(row.getCell(configuratorProperties.getRule().getCategoryColumn()).getStringCellValue())
                    .subcategory(row.getCell(configuratorProperties.getRule().getSubcategoryColumn()).getStringCellValue())
                    .label(row.getCell(configuratorProperties.getRule().getLabelColumn()).getStringCellValue())
                    .skipTransaction(row.getCell(configuratorProperties.getRule().getSkipTransactionColumn()).getBooleanCellValue())
                    .build();
                rules.add(ruleDto);
            }
        }
        return rules;
    }

    private Set<StatementSettingDto> getStatementSettings(final Workbook workbook) {
        Assert.notNull(configuratorProperties.getStatementSetting().getSheetName(), "configurator.statement-setting.sheet-name must be set.");
        Assert.notNull(configuratorProperties.getStatementSetting().getNameColumn(), "configurator.statement-setting.name-column must be set.");
        Assert.notNull(configuratorProperties.getStatementSetting().getCharsetColumn(), "configurator.statement-setting.charset-column must be set.");
        Assert.notNull(configuratorProperties.getStatementSetting().getAccountColumn(), "configurator.statement-setting.account-column must be set.");
        Assert.notNull(configuratorProperties.getStatementSetting().getDatePatternColumn(), "configurator.statement-setting.date-pattern-column must be set.");
        Assert.notNull(configuratorProperties.getStatementSetting().getSkipLinesColumn(), "configurator.statement-setting.skip-lines-column must be set.");
        Assert.notNull(configuratorProperties.getStatementSetting().getDateColumn(), "configurator.statement-setting.date-column must be set.");
        Assert.notNull(configuratorProperties.getStatementSetting().getRecipientColumn(), "configurator.statement-setting.recipient-column must be set.");
        Assert.notNull(configuratorProperties.getStatementSetting().getAccountNumberColumn(), "configurator.statement-setting.account-number-column must be set.");
        Assert.notNull(configuratorProperties.getStatementSetting().getBicCodeColumn(), "configurator.statement-setting.bic-code-column must be set.");
        Assert.notNull(configuratorProperties.getStatementSetting().getNoteColumn(), "configurator.statement-setting.note-column must be set.");
        Assert.notNull(configuratorProperties.getStatementSetting().getAmountColumn(), "configurator.statement-setting.amount-column must be set.");
        Assert.notNull(configuratorProperties.getStatementSetting().getCurrencyColumn(), "configurator.statement-setting.currency-column must be set.");

        final Sheet statementSettingsSheet = workbook.getSheet(configuratorProperties.getStatementSetting().getSheetName());
        final Set<StatementSettingDto> statementSettings = new HashSet<>();
        for (Row row : statementSettingsSheet) {
            if (row.getRowNum() == 0) {
                continue; // Skip header
            }
            if (!row.getCell(configuratorProperties.getStatementSetting().getNameColumn()).getStringCellValue().isBlank()) {
                final StatementSettingDto statementSettingDto = StatementSettingDto.builder()
                    .name(row.getCell(configuratorProperties.getStatementSetting().getNameColumn()).getStringCellValue())
                    .charset(row.getCell(configuratorProperties.getStatementSetting().getCharsetColumn()).getStringCellValue())
                    .account(row.getCell(configuratorProperties.getStatementSetting().getAccountColumn()).getStringCellValue())
                    .datePattern(row.getCell(configuratorProperties.getStatementSetting().getDatePatternColumn()).getStringCellValue())
                    .skipLines((int) row.getCell(configuratorProperties.getStatementSetting().getSkipLinesColumn()).getNumericCellValue())
                    .dateColumn((int) row.getCell(configuratorProperties.getStatementSetting().getDateColumn()).getNumericCellValue())
                    .recipientColumn((int) row.getCell(configuratorProperties.getStatementSetting().getRecipientColumn()).getNumericCellValue())
                    .accountNumberColumn((int) row.getCell(configuratorProperties.getStatementSetting().getAccountNumberColumn()).getNumericCellValue())
                    .bicCodeColumn((int) row.getCell(configuratorProperties.getStatementSetting().getBicCodeColumn()).getNumericCellValue())
                    .noteColumn(row.getCell(configuratorProperties.getStatementSetting().getNoteColumn()).getStringCellValue())
                    .amountColumn((int) row.getCell(configuratorProperties.getStatementSetting().getAmountColumn()).getNumericCellValue())
                    .currency(row.getCell(configuratorProperties.getStatementSetting().getCurrencyColumn()).getStringCellValue())
                    .build();
                statementSettings.add(statementSettingDto);
            }
        }
        return statementSettings;
    }

    private void setupAccounts(final Set<String> accounts) {
        accounts.forEach(parsedAccount -> {
            final AccountDto account = new AccountDto();
            account.setName(parsedAccount);
            final AccountDto createdAccount = accountFacade.createAccount(account);
            log.info("Account saved '{}'", createdAccount);
        });
    }

    private void setupCategories(final Set<String> categories) {
        categories.forEach(parsedCategory -> {
            final CategoryDto category = new CategoryDto();
            category.setName(parsedCategory);
            final CategoryDto createdCategory = categoryFacade.createCategory(category);
            log.info("Category saved '{}'", createdCategory);
        });
    }

    private void setupSubcategories(final Map<String, String> subcategories) {
        subcategories.forEach((subcategory, category) -> {
            final SubcategoryDto subcategoryDto = new SubcategoryDto();
            subcategoryDto.setName(subcategory);
            subcategoryDto.setCategory(category);

            final SubcategoryDto createdSubcategory;
            try {
                createdSubcategory = subcategoryFacade.createSubcategory(subcategoryDto);
            } catch (ResourceNotFoundException e) {
                log.error("Error creating subcategory: '{}' in category: '{}'", subcategory, category);
                throw new RuntimeException(e);
            }
            log.info("Subcategory saved '{}'", createdSubcategory);
        });
    }

    private void setupRules(final Set<RuleDto> rules) {
        rules.forEach(ruleDto -> {
            try {
                final RuleDto createdRule = ruleFacade.createRule(ruleDto);
                log.info("Rule saved '{}'", createdRule);
            } catch (ResourceNotFoundException e) {
                log.error("Error creating rule: '{}'", ruleDto);
                throw new RuntimeException(e);
            }
        });
    }

    private void setupStatementSettings(final Set<StatementSettingDto> statementSettings) {
        statementSettings.forEach(statementSettingDto -> {
            try {
                final StatementSettingDto createdStatementSetting = statementSettingFacade.createStatementSetting(statementSettingDto);
                log.info("Statement setting saved '{}'", createdStatementSetting);
            } catch (ResourceNotFoundException e) {
                log.error("Error creating statement setting: '{}'", statementSettingDto);
                throw new RuntimeException(e);
            }
        });
    }
}
