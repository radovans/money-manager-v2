package cz.sinko.moneymanager.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import cz.sinko.moneymanager.config.exception.ResourceNotFoundException;
import cz.sinko.moneymanager.facade.dto.RuleDto;
import cz.sinko.moneymanager.facade.mapper.RuleMapper;
import cz.sinko.moneymanager.repository.RuleRepository;
import cz.sinko.moneymanager.repository.model.Category;
import cz.sinko.moneymanager.repository.model.Rule;
import cz.sinko.moneymanager.repository.model.Subcategory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * Service for rule operations.
 *
 * @author Sinko Radovan
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RuleService {

    private final RuleRepository ruleRepository;

    /**
     * Create rule.
     *
     * @param ruleDto rule DTO
     * @param category category
     * @param subcategory subcategory
     * @return created rule
     */
    public Rule createRule(final RuleDto ruleDto, final Category category, final Subcategory subcategory) {
        Rule rule = RuleMapper.t().map(ruleDto);
        if (!ruleDto.isSkipTransaction()) {
            if (category != null) {
                rule.setCategory(category);
            }
            if (subcategory != null) {
                rule.setSubcategory(subcategory);
                rule.setCategory(subcategory.getCategory());
            }
        }
        return ruleRepository.save(rule);
    }

    /**
     * Find rule by ID.
     *
     * @param ruleId rule ID
     * @return rule
     * @throws ResourceNotFoundException if rule was not found
     */
    public Rule find(final Long ruleId) throws ResourceNotFoundException {
        return ruleRepository.findById(ruleId).orElseThrow(() -> ResourceNotFoundException.createWith(
            "Rule",
            " with id '" + ruleId + "' was not found"));
    }

    /**
     * Find all rules.
     *
     * @return list of rules
     */
    public List<Rule> find() {
        return ruleRepository.findAll();
    }

    /**
     * Find all rules.
     *
     * @param sort sort
     * @return list of rules
     */
    public List<Rule> find(final Sort sort) {
        return ruleRepository.findAll(sort);
    }

    /**
     * Update rule.
     *
     * @param id rule ID
     * @param ruleDto rule DTO
     * @param category category
     * @param subcategory subcategory
     * @return updated rule
     * @throws ResourceNotFoundException if rule was not found
     */
    public Rule updateRule(final Long id, final RuleDto ruleDto, final Category category, final Subcategory subcategory)
        throws ResourceNotFoundException {
        Rule rule = find(id);
        RuleMapper.t().update(rule, ruleDto);
        if (!ruleDto.isSkipTransaction()) {
            if (category != null) {
                rule.setCategory(category);
            }
            if (subcategory != null) {
                rule.setSubcategory(subcategory);
            }
        } else {
            rule.setSubcategory(null);
            rule.setCategory(null);
        }
        return ruleRepository.save(rule);
    }

    /**
     * Delete rule.
     *
     * @param id rule ID
     */
    public void deleteRule(final Long id) {
        ruleRepository.deleteById(id);
    }
}