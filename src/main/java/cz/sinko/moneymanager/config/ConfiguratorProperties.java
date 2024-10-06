package cz.sinko.moneymanager.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;


/**
 * @author Sinko Radovan
 */
@ConfigurationProperties(prefix = ConfigurationConstants.CONFIGURATOR_PREFIX)
@Data
public class ConfiguratorProperties {

    private String filePath;

    private AccountProperties account;

    private CategoryProperties category;

    private SubcategoryProperties subcategory;

    private RuleProperties rule;

    @Data
    public static class AccountProperties {
        private String sheetName;

        private Integer nameColumn;
    }

    @Data
    public static class CategoryProperties {
        private String sheetName;

        private Integer nameColumn;
    }

    @Data
    public static class SubcategoryProperties {
        private String sheetName;

        private Integer nameColumn;

        private Integer categoryColumn;
    }

    @Data
    public static class RuleProperties {
        private String sheetName;

        private Integer keyColumn;

        private Integer typeColumn;

        private Integer recipientColumn;

        private Integer noteColumn;

        private Integer categoryColumn;

        private Integer subcategoryColumn;

        private Integer labelColumn;

        private Integer skipTransactionColumn;
    }
}
