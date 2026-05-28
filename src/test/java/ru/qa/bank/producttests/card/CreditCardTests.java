package ru.qa.bank.producttests.card;

import org.junit.jupiter.api.Test;
import ru.qa.bank.exception.InsufficientFundsException;
import ru.qa.bank.exception.InvalidAmountException;
import ru.qa.bank.model.Currency;
import ru.qa.bank.product.card.CreditCard;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class CreditCardTests {
    @Test
    void createCardTest() {
        CreditCard card = creditCard();

        assertSoftly(softly -> {
            softly.assertThat(card.getName()).isEqualTo("Credit card");
            softly.assertThat(card.getCurrency()).isEqualTo(Currency.RUB);
            softly.assertThat(card.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(1_000));
            softly.assertThat(card.getInterestRate()).isEqualByComparingTo(BigDecimal.valueOf(19.9));
            softly.assertThat(card.getCreditLimit()).isEqualByComparingTo(BigDecimal.valueOf(5_000));
            softly.assertThat(card.getDebt()).isEqualByComparingTo(BigDecimal.ZERO);
            softly.assertThat(card.getAvailableFunds()).isEqualByComparingTo(BigDecimal.valueOf(6_000));
        });
    }

    @Test
    void withdrawFromBalanceTest() {
        CreditCard card = creditCard();

        card.withdraw(BigDecimal.valueOf(300));

        assertSoftly(softly -> {
            softly.assertThat(card.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(700));
            softly.assertThat(card.getDebt()).isEqualByComparingTo(BigDecimal.ZERO);
            softly.assertThat(card.getAvailableFunds()).isEqualByComparingTo(BigDecimal.valueOf(5_700));
        });
    }

    @Test
    void withdrawFromCreditLimitTest() {
        CreditCard card = creditCard();

        card.withdraw(BigDecimal.valueOf(1_500));

        assertSoftly(softly -> {
            softly.assertThat(card.getBalance()).isEqualByComparingTo(BigDecimal.ZERO);
            softly.assertThat(card.getDebt()).isEqualByComparingTo(BigDecimal.valueOf(500));
            softly.assertThat(card.getAvailableFunds()).isEqualByComparingTo(BigDecimal.valueOf(4_500));
        });
    }

    @Test
    void depositRepaysDebtFirstTest() {
        CreditCard card = creditCard();

        card.withdraw(BigDecimal.valueOf(1_500));
        card.deposit(BigDecimal.valueOf(700));

        assertSoftly(softly -> {
            softly.assertThat(card.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(200));
            softly.assertThat(card.getDebt()).isEqualByComparingTo(BigDecimal.ZERO);
            softly.assertThat(card.getAvailableFunds()).isEqualByComparingTo(BigDecimal.valueOf(5_200));
        });
    }

    @Test
    void withdrawOverLimitRejectedTest() {
        CreditCard card = creditCard();

        assertThatThrownBy(() -> card.withdraw(BigDecimal.valueOf(6_001)))
                .isInstanceOf(InsufficientFundsException.class)
                .hasMessage("Credit limit exceeded");
    }

    @Test
    void zeroInterestRateRejectedTest() {
        assertThatThrownBy(() -> new CreditCard(
                "Credit card",
                Currency.RUB,
                BigDecimal.valueOf(1_000),
                BigDecimal.ZERO,
                BigDecimal.valueOf(5_000)
        ))
                .isInstanceOf(InvalidAmountException.class)
                .hasMessage("Interest rate must be positive");
    }

    @Test
    void zeroCreditLimitRejectedTest() {
        assertThatThrownBy(() -> new CreditCard(
                "Credit card",
                Currency.RUB,
                BigDecimal.valueOf(1_000),
                BigDecimal.valueOf(19.9),
                BigDecimal.ZERO
        ))
                .isInstanceOf(InvalidAmountException.class)
                .hasMessage("Amount must be positive");
    }

    private CreditCard creditCard() {
        return new CreditCard(
                "Credit card",
                Currency.RUB,
                BigDecimal.valueOf(1_000),
                BigDecimal.valueOf(19.9),
                BigDecimal.valueOf(5_000)
        );
    }
}
