package cz.sinko.moneymanager.facade.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import cz.sinko.moneymanager.facade.dto.SubcategoryDto;
import cz.sinko.moneymanager.repository.model.Subcategory;


/**
 * Mapper for Subcategory entity.
 *
 * @author Sinko Radovan
 */
@Mapper()
public interface SubcategoryMapper {

    /**
     * Get instance of SubcategoryMapper.
     *
     * @return instance of SubcategoryMapper
     */
    static SubcategoryMapper t() {
        return Mappers.getMapper(SubcategoryMapper.class);
    }

    /**
     * Map Subcategory to SubcategoryDto.
     *
     * @param source subcategory
     * @return subcategoryDto
     */
    @Mapping(target = "category", source = "category.name")
    SubcategoryDto map(final Subcategory source);

    /**
     * Map SubcategoryDto to Subcategory.
     *
     * @param source subcategoryDto
     * @return subcategory
     */
    @Mapping(target = "category", ignore = true)
    Subcategory map(final SubcategoryDto source);

    /**
     * Map list of subcategories to list of subcategoryDto.
     *
     * @param source list of subcategories
     * @return list of subcategoryDto
     */
    List<SubcategoryDto> map(final List<Subcategory> source);
}
