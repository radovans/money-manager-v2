package cz.sinko.moneymanager.facade;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import cz.sinko.moneymanager.config.exception.ResourceNotFoundException;
import cz.sinko.moneymanager.facade.dto.RuleDto;
import cz.sinko.moneymanager.facade.mapper.RuleMapper;
import cz.sinko.moneymanager.repository.model.Category;
import cz.sinko.moneymanager.repository.model.Rule;
import cz.sinko.moneymanager.repository.model.RuleType;
import cz.sinko.moneymanager.repository.model.Subcategory;
import cz.sinko.moneymanager.repository.model.Transaction;
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

    public List<Transaction> applyRules(List<Transaction> transactions) {
        final List<Rule> rules = ruleService.find();
        final Map<String, Rule> rulesBasedOnAccount =
            rules.stream().filter(rule -> RuleType.ACCOUNT.equals(rule.getType())).collect(Collectors.toMap
                (Rule::getKey, Function.identity()));
        final Map<String, Rule> rulesBasedOnNote = rules.stream().filter(rule -> RuleType.NOTE.equals(rule.getType())).collect(Collectors.toMap
            (Rule::getKey, Function.identity()));
        final Map<String, Rule> rulesBasedOnRecipient = rules.stream().filter(rule -> RuleType.RECIPIENT.equals(rule.getType())).collect
            (Collectors.toMap(Rule::getKey, Function.identity()));

        List<Transaction> result = new ArrayList<>();

        transactions.forEach(transaction -> {
            try {
                final Optional<String> keyBasedOnAccount = rulesBasedOnAccount.keySet()
                    .parallelStream()
                    .filter(rule -> rule.equals(transaction.getAccountNumber() + "/" + transaction.getBicCode()))
                    .findFirst();
                final Optional<String> keyBasedOnNote = rulesBasedOnNote.keySet().parallelStream().filter(transaction.getNote()::contains)
                    .findFirst();
                final Optional<String> keyBasedOnRecipient = rulesBasedOnRecipient.keySet().parallelStream().filter(transaction.getRecipient()
                    ::contains).findFirst();

                if (keyBasedOnAccount.isEmpty() && keyBasedOnNote.isEmpty() && keyBasedOnRecipient.isEmpty()) {
                    result.add(transaction);
                } else {
                    Transaction updatedTransaction = null;
                    if (keyBasedOnAccount.isPresent()) {
                        updatedTransaction = applyRule(transaction, keyBasedOnAccount, rulesBasedOnAccount).orElse(null);
                    }
                    if (keyBasedOnNote.isPresent()) {
                        updatedTransaction = applyRule(transaction, keyBasedOnNote, rulesBasedOnNote).orElse(null);
                    }
                    if (keyBasedOnRecipient.isPresent()) {
                        updatedTransaction = applyRule(transaction, keyBasedOnRecipient, rulesBasedOnRecipient).orElse(null);
                    }

                    if (updatedTransaction != null) {
                        result.add(updatedTransaction);
                    }
                }
            } catch (ResourceNotFoundException e) {
                throw new RuntimeException("Subcategory or Category not found", e);
            }
        });

        return result;
    }

    private Optional<Transaction> applyRule(Transaction transaction, Optional<String> key, Map<String, Rule> rules)
        throws ResourceNotFoundException {
        Transaction updatedTransaction = Transaction.builder()
            .id(transaction.getId())
            .date(transaction.getDate())
            .recipient(transaction.getRecipient())
            .note(transaction.getNote())
            .accountNumber(transaction.getAccountNumber())
            .bicCode(transaction.getBicCode())
            .amount(transaction.getAmount())
            .amountInCzk(transaction.getAmountInCzk())
            .currency(transaction.getCurrency())
            .category(transaction.getCategory())
            .subcategory(transaction.getSubcategory())
            .account(transaction.getAccount())
            .label(transaction.getLabel())
            .transactionType(transaction.getTransactionType())
            .build();

        if (key.isPresent()) {
            Rule rule = rules.get(key.get());
            if (rule.isSkipTransaction()) {
                return Optional.empty();
            }

            if (!rule.getRecipient().isBlank()) {
                updatedTransaction.setRecipient(rule.getRecipient());
            }
            if (!rule.getNote().isBlank()) {
                updatedTransaction.setNote(rule.getNote());
            }
            if (rule.getCategory() != null) {
                updatedTransaction.setCategory(categoryService.find(rule.getCategory().getName()));
            }
            if (rule.getSubcategory() != null) {
                updatedTransaction.setSubcategory(subcategoryService.find(rule.getSubcategory().getName()));
            }
            if (!rule.getLabel().isBlank()) {
                updatedTransaction.setLabel(rule.getLabel());
            }
        }

        return Optional.of(updatedTransaction);
    }
}
