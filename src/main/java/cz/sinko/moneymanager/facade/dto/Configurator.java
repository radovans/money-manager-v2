package cz.sinko.moneymanager.facade.dto;

import java.util.Map;
import java.util.Set;

import lombok.Data;


/**
 * Configurator for the application.
 *
 * @author Sinko Radovan
 */
@Data
public class Configurator {

    private Set<String> accounts;

    private Set<String> categories;

    private Map<String, String> subcategories;

    private Set<RuleDto> rules;

    private Set<StatementSettingDto> statementSettings;
}
