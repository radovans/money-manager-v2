package cz.sinko.moneymanager.facade.dto;

import cz.sinko.moneymanager.repository.model.RuleType;
import lombok.Builder;
import lombok.Data;


/**
 * DTO for rule.
 *
 * @author Sinko Radovan
 */
@Builder
@Data
public class RuleDto {

    private Long id;

    private String key;

    private RuleType type;

    private String recipient;

    private String note;

    private String category;

    private String subcategory;

    private String label;

    private boolean skipTransaction;
}
