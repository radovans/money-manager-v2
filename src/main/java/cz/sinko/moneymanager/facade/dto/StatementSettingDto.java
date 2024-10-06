package cz.sinko.moneymanager.facade.dto;

import lombok.Builder;
import lombok.Data;


/**
 * DTO for import statement setting.
 *
 * @author Sinko Radovan
 */
@Builder
@Data
public class StatementSettingDto {

    private Long id;

    private String name;

    private String charset;

    private String account;

    private String datePattern;

    private int skipLines;

    private int dateColumn;

    private int recipientColumn;

    private int accountNumberColumn;

    private int bicCodeColumn;

    private String noteColumn;

    private int amountColumn;

    private String currency;
}
