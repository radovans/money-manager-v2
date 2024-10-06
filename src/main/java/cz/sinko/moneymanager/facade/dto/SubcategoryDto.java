package cz.sinko.moneymanager.facade.dto;

import lombok.Data;


/**
 * DTO for subcategory.
 *
 * @author Sinko Radovan
 */
@Data
public class SubcategoryDto {

    private Long id;

    private String name;

    private String category;
}
