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

import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Model for storing rules for statement parsing settings.
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

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @NotBlank
    private String datePattern;

    @NotNull
    @Positive
    private int skipLines;

    @NotNull
    @Positive
    private int dateColumn;

    @NotNull
    @Positive
    private int recipientColumn;

    @NotNull
    @Positive
    private int noteColumn;

    @NotNull
    @Positive
    private int amountColumn;

    @NotNull
    @Positive
    private int currencyColumn;
}
