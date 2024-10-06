package cz.sinko.moneymanager.facade.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import cz.sinko.moneymanager.facade.dto.StatementSettingDto;
import cz.sinko.moneymanager.repository.model.StatementSetting;


/**
 * Mapper for StatementSetting entity.
 *
 * @author Sinko Radovan
 */
@Mapper(uses = { AccountMapper.class })
public interface StatementSettingMapper {

	/**
	 * Get instance of StatementSettingMapper.
	 *
	 * @return instance of StatementSettingMapper
	 */
	static StatementSettingMapper t() {
		return Mappers.getMapper(StatementSettingMapper.class);
	}

	/**
	 * Map StatementSetting to StatementSettingDto.
	 *
	 * @param source statementSetting
	 * @return statementSettingDto
	 */
	@Mapping(target = "account", source = "account.name")
	StatementSettingDto map(final StatementSetting source);

	/**
	 * Map StatementSettingDto to StatementSetting.
	 *
	 * @param source statementSettingDto
	 * @return statementSetting
	 */

	@Mapping(target = "account", ignore = true)
	StatementSetting map(final StatementSettingDto source);
}
