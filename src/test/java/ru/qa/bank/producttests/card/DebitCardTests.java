package ru.qa.bank.producttests.card;

import org.junit.jupiter.api.Test;
import ru.qa.bank.exception.InsufficientFundsException;
import ru.qa.bank.exception.InvalidAmountException;
import ru.qa.bank.model.Currency;
import ru.qa.bank.product.card.DebitCard;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class DebitCardTests {
    @Test
    void createCardTest() {
        DebitCard card = new DebitCard("Debit card", Currency.RUB, BigDecimal.valueOf(1_000));

        assertSoftly(softly -> {
            softly.assertThat(card.getName()).isEqualTo("Debit card");
            softly.assertThat(card.getCurrency()).isEqualTo(Currency.RUB);
            softly.assertThat(card.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(1_000));
        });
    }

    @Test
    void depositIncreasesBalanceTest() {
        DebitCard card = new DebitCard("Debit card", Currency.RUB, BigDecimal.valueOf(1_000));

        card.deposit(BigDecimal.valueOf(500));

        assertSoftly(softly ->
                softly.assertThat(card.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(1_500))
        );
    }

    @Test
    void withdrawDecreasesBalanceTest() {
        DebitCard card = new DebitCard("Debit card", Currency.RUB, BigDecimal.valueOf(1_000));

        card.withdraw(BigDecimal.valueOf(400));

        assertSoftly(softly ->
                softly.assertThat(card.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(600))
        );
    }

    @Test
    void withdrawOverBalanceRejectedTest() {
        DebitCard card = new DebitCard("Debit card", Currency.RUB, BigDecimal.valueOf(1_000));

        assertThatThrownBy(() -> card.withdraw(BigDecimal.valueOf(1_001)))
                .isInstanceOf(InsufficientFundsException.class)
                .hasMessage("Insufficient funds");
    }

    @Test
    void invalidAmountRejectedTest() {
        DebitCard card = new DebitCard("Debit card", Currency.RUB, BigDecimal.valueOf(1_000));

        assertThatThrownBy(() -> card.deposit(BigDecimal.ZERO))
                .isInstanceOf(InvalidAmountException.class)
                .hasMessage("Amount must be positive");

        assertThatThrownBy(() -> card.withdraw(BigDecimal.valueOf(-100)))
                .isInstanceOf(InvalidAmountException.class)
                .hasMessage("Amount must be positive");
    }
}
