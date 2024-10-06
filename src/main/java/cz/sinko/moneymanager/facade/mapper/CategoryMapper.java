package cz.sinko.moneymanager.facade.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import cz.sinko.moneymanager.facade.dto.CategoryDto;
import cz.sinko.moneymanager.repository.model.Category;

/**
 * Mapper for Category entity.
 *
 * @author Sinko Radovan
 */
@Mapper()
public interface CategoryMapper {

	/**
	 * Get instance of CategoryMapper.
	 *
	 * @return instance of CategoryMapper
	 */
	static CategoryMapper t() {
		return Mappers.getMapper(CategoryMapper.class);
	}

	/**
	 * Map Category to CategoryDto.
	 *
	 * @param source category
	 * @return categoryDto
	 */
	CategoryDto map(final Category source);

	/**
	 * Map CategoryDto to Category.
	 *
	 * @param categoryDto categoryDto
	 * @return category
	 */
	Category map(final CategoryDto categoryDto);

	/**
	 * Map list of categories to list of categoryDto.
	 *
	 * @param source list of categories
	 * @return list of categoryDto
	 */
	List<CategoryDto> map(final List<Category> source);
}
