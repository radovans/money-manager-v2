package cz.sinko.moneymanager.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import cz.sinko.moneymanager.config.exception.ResourceNotFoundException;
import cz.sinko.moneymanager.facade.dto.RuleDto;
import cz.sinko.moneymanager.facade.mapper.RuleMapper;
import cz.sinko.moneymanager.repository.model.Category;
import cz.sinko.moneymanager.repository.model.Subcategory;
import cz.sinko.moneymanager.service.CategoryService;
import cz.sinko.moneymanager.service.RuleService;
import cz.sinko.moneymanager.service.SubcategoryService;
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
public class RuleFacade {

    private final RuleService ruleService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SubcategoryService subcategoryService;

    /**
     * Create rule.
     *
     * @param ruleDto rule DTO
     * @return created rule DTO
     * @throws ResourceNotFoundException if category or subcategory was not found
     */
    public RuleDto createRule(final RuleDto ruleDto) throws ResourceNotFoundException {
        Subcategory subcategory = null;
        Category category = null;

        if (!ruleDto.getCategory().isBlank()) {
            category = categoryService.find(ruleDto.getCategory());
        }
        if (!ruleDto.getSubcategory().isBlank()) {
            subcategory = subcategoryService.find(ruleDto.getSubcategory());
        }

        return RuleMapper.t().map(ruleService.createRule(ruleDto, category, subcategory));
    }

    /**
     * Get all rules.
     *
     * @return list of rules
     */
    public List<RuleDto> getRules() {
        return RuleMapper.t().map(ruleService.find(Sort.by("id").ascending()));
    }

    /**
     * Update rule.
     *
     * @param id rule ID
     * @param ruleDto rule DTO
     * @return updated rule DTO
     * @throws ResourceNotFoundException if category or subcategory was not found
     */
    public RuleDto updateRule(final Long id, final RuleDto ruleDto) throws ResourceNotFoundException {
        final Subcategory subcategory = subcategoryService.find(ruleDto.getSubcategory());
        final Category category = categoryService.find(ruleDto.getCategory());
        return RuleMapper.t().map(ruleService.updateRule(id, ruleDto, category, subcategory));
    }

    /**
     * Delete rule.
     *
     * @param id rule ID
     */
    public void deleteRule(final Long id) {
        ruleService.deleteRule(id);
    }

    //	public List<Transaction> applyRules(List<Transaction> transactions) {
    //		List<Rule> rules = ruleService.find();
    //		Map<String, Rule> rulesBasedOnNote = rules.stream().filter(rule -> RuleType.NOTE.equals(rule.getType())).collect(Collectors.toMap
    //		(Rule::getKey, Function.identity()));
    //		Map<String, Rule> rulesBasedOnRecipient = rules.stream().filter(rule -> RuleType.RECIPIENT.equals(rule.getType())).collect
    //		(Collectors.toMap(Rule::getKey, Function.identity()));
    //		transactions.forEach(transaction -> {
    //			try {
    //				Optional<String> keyBasedOnNote = rulesBasedOnNote.keySet().parallelStream().filter(transaction.getNote()::contains)
    //				.findFirst();
    //				applyRule(transaction, keyBasedOnNote, rulesBasedOnNote);
    //				Optional<String> keyBasedOnRecipient = rulesBasedOnRecipient.keySet().parallelStream().filter(transaction.getRecipient()
    //				::contains).findFirst();
    //				applyRule(transaction, keyBasedOnRecipient, rulesBasedOnRecipient);
    //			} catch (ResourceNotFoundException e) {
    //				throw new RuntimeException("Category or main category not found", e);
    //			}
    //		});
    //		return transactions;
    //	}

    //	private void applyRule(Transaction transaction, Optional<String> key, Map<String, Rule> rules)
    //			throws ResourceNotFoundException {
    //		if (key.isPresent()) {
    //			Rule rule = rules.get(key.get());
    //			if (rule.getRecipient() != null) {
    //				transaction.setRecipient(rule.getRecipient());
    //			}
    //			if (rule.getNote() != null) {
    //				transaction.setNote(rule.getNote());
    //			}
    //			if (rule.getCategory() != null) {
    //				transaction.setCategory(categoryService.find(rule.getCategory().getName()));
    //			}
    //			if (rule.getSubcategory() != null) {
    //				transaction.setSubcategory(subcategoryService.find(rule.getSubcategory().getName()));
    //			}
    //			if (rule.getLabel() != null) {
    //				transaction.setLabel(rule.getLabel());
    //			}
    //		}
    //	}
}
