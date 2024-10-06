package cz.sinko.moneymanager.facade.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import cz.sinko.moneymanager.facade.dto.RuleDto;
import cz.sinko.moneymanager.repository.model.Rule;

/**
 * Mapper for Rule entity.
 *
 * @author Sinko Radovan
 */
@Mapper(uses = { CategoryMapper.class })
public interface RuleMapper {

	/**
	 * Get instance of RuleMapper.
	 *
	 * @return instance of RuleMapper
	 */
	static RuleMapper t() {
		return Mappers.getMapper(RuleMapper.class);
	}

	/**
	 * Map Rule to RuleDto.
	 *
	 * @param source rule
	 * @return ruleDto
	 */
	@Mapping(target = "subcategory", source = "subcategory.name")
	@Mapping(target = "category", source = "category.name")
	RuleDto map(final Rule source);

	/**
	 * Map RuleDto to Rule.
	 *
	 * @param source ruleDto
	 * @return rule
	 */
	@Mapping(target = "subcategory", ignore = true)
	@Mapping(target = "category", ignore = true)
	Rule map(final RuleDto source);

	/**
	 * Map list of rules to list of ruleDto.
	 *
	 * @param source list of rules
	 * @return list of ruleDto
	 */
	List<RuleDto> map(final List<Rule> source);

	/**
	 * Update rule entity with data from ruleDto.
	 *
	 * @param entity rule entity
	 * @param updateEntity ruleDto
	 */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "subcategory", ignore = true)
	@Mapping(target = "category", ignore = true)
	void update(@MappingTarget final Rule entity, final RuleDto updateEntity);
}
