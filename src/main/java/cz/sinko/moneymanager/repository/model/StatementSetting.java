package cz.sinko.moneymanager.repository.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Model for storing statement settings.
 *
 * @author Sinko Radovan
 */
@Data
@Entity
@NoArgsConstructor
public class StatementSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String charset;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @NotBlank
    private String datePattern;

    @NotNull
    @PositiveOrZero
    private int skipLines;

    @NotNull
    @PositiveOrZero
    private int dateColumn;

    @NotNull
    @PositiveOrZero
    private int recipientColumn;

    @NotNull
    @PositiveOrZero
    private int accountNumberColumn;

    @NotNull
    @PositiveOrZero
    private int bicCodeColumn;

    @NotBlank
    private String noteColumn;

    @NotNull
    @PositiveOrZero
    private int amountColumn;

    @NotBlank
    private String currency;
}
